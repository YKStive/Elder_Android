package lilun.com.pension.module.utils.mqtt;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.User;
import lilun.com.pension.module.utils.DeviceUtils;
import lilun.com.pension.module.utils.StringUtils;

/**
 * 需要展示到通知栏的mqtt消息
 * 因为格式不一致，具体约定参见：https://oa.liluntech.com/projects/sce/wiki/%E6%8E%A8%E9%80%81%E8%A7%84%E8%8C%83%E7%BA%A6%E5%AE%9A
 *
 * @author yk
 *         create at 2017/6/7 15:34
 *         email : yk_developer@163.com
 */
public class MqttNotificationHelper {

    public void showOnNotification(String topic, String data) {
        MqttTopic mqttTopic = new MqttTopic();

        JSONObject jsonObject = JSON.parseObject(data);
        String title = null;
        String content = null;

        //公告和普通求助
        if (TextUtils.equals(topic, mqttTopic.normal_announce) || TextUtils.equals(topic, mqttTopic.normal_help)) {
            JSONObject infoJson = jsonObject.getJSONObject("data");
            String organizationId = infoJson.getString("organizationId");
            if (TextUtils.isEmpty(organizationId)) return;
            String targetId = StringUtils.removeSpecialSuffix(organizationId);

            boolean canOperate = User.canOperate(targetId);


            //如果是在当前组织的市级以下的消息才处理，不然直接遗弃
            if (canOperate) {

                //发送事件，展示到app
                EventBus.getDefault().post(new Event.BoardMsg(topic, data));


                // 公告，展示到通知栏
                if (TextUtils.equals(topic, mqttTopic.normal_announce)) {
                    String parentId = infoJson.getString("parentId");
                    if (parentId.endsWith("社区公告")) {
                        title = "公告";
                        content = infoJson.getString("name");
                    }
                }


                //普通求助
                if (TextUtils.equals(topic, mqttTopic.normal_help)) {
                    EventBus.getDefault().post(new Event.RefreshHelpData());
                }
            }
        }


        //紧急消息
        if (TextUtils.equals(topic, mqttTopic.urgent_help)) {
            title = "紧急求助";
            content = jsonObject.getString("message");

            //发送事件，展示到app
            EventBus.getDefault().post(new Event.BoardMsg(topic, data));
        }


        //登陆
        if (TextUtils.equals(topic, mqttTopic.login)) {
            dealLogin(data);
        }


        show(title, content);

    }


    /**
     * 处理登陆
     */
    private void dealLogin(String messageData) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(messageData);
            String from = jsonObject.getString("from");
            String time = jsonObject.getString("time");
            if (!TextUtils.isEmpty(from)) {
                String clientId = DeviceUtils.getUniqueIdForThisApp(App.context);
                if (!TextUtils.equals(from, clientId)) {
//                        Logger.i("不同设备登陆，此设备下线"+"两个设备id--" + "from--" + from + "---" + "clientId" + clientId);
                    //只有在登录之后的  请求踢账号才有效
                    if (App.loginDate != null && App.loginDate.before(StringUtils.string2Date(time)))
                        EventBus.getDefault().post(new Event.OffLine());
                }
            } else {
//                    Logger.i("相同设备登陆");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 通知栏显示
     */
    private void show(String title, String content) {
        if (!TextUtils.isEmpty(title)) {
            Notification build = new NotificationCompat.Builder(App.context)
                    .setSmallIcon(R.drawable.small_icon)
                    .setContentTitle(title)
                    .setContentText(content).build();

            NotificationManager manager =
                    (NotificationManager) App.context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0x01, build);

            wakeScreen();
        }
    }

    /**
     * 唤醒屏幕
     */
    private void wakeScreen() {
        PowerManager pm = (PowerManager) App.context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (!isScreenOn) {
            PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
            wakeLock.acquire();
            wakeLock.release();
        }

    }
}
