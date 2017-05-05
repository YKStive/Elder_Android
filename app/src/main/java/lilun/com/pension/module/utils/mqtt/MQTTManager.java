package lilun.com.pension.module.utils.mqtt;


import android.util.Log;

import com.orhanobut.logger.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import lilun.com.pension.app.App;
import lilun.com.pension.app.Config;
import lilun.com.pension.module.utils.DeviceUtils;


/**
 * MQTT管理
 *
 * @author yk
 *         create at 2017/4/25 9:46
 *         email : yk_developer@163.com
 */
public class MQTTManager {

    // 单例
    private static MQTTManager mInstance = null;

    // 回调
    private MqttCallback mCallback;

    private MqttAndroidClient client;

    private MQTTManager() {
        mCallback = new MQTTCallbackBus();
    }

    public static MQTTManager getInstance() {
        if (null == mInstance) {
            mInstance = new MQTTManager();
        }
        return mInstance;
    }

    /**
     * 释放单例, 及其所引用的资源
     */
    public static void release() {
        try {
            if (mInstance != null) {
                mInstance.disConnect();
                mInstance = null;
            }
        } catch (Exception e) {

        }
    }

    public boolean isConnected() {

        return client != null && client.isConnected();
    }

    public void createConnect(String userName, String password, String[] topics, int[] qos) {
        if (client != null && client.isConnected()) {
            Logger.i("mqtt 已经链接不需要再次链接");
            return;
        }
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        if (password != null) {
            mqttConnectOptions.setPassword(password.toCharArray());
        }
        if (userName != null) {
            mqttConnectOptions.setUserName(userName);
        }

        if (client == null) {
            String deviceId = new DeviceUtils(App.context).getUniqueID();
            client = new MqttAndroidClient(App.context, Config.MQTT_URL, deviceId);
            Logger.i("设备Id:" + deviceId);
        }

        client.setCallback(mCallback);
        try {
            client.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Logger.i("连接mqtt服务器成功");
                    subscribe(topics, qos);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Logger.i("链接失败" + exception.getMessage());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }


    public void publish(String publishTopic, int qos, String publishMessage) {
        if (client != null && client.isConnected()) {
            try {
                MqttMessage message = new MqttMessage();
                message.setQos(qos);
                message.setPayload(publishMessage.getBytes());
                IMqttDeliveryToken token = client.publish(publishTopic, message);
                Logger.i("发送数据:" + token.getMessage());
                if (!client.isConnected()) {
                }
            } catch (MqttException e) {
                Log.d("yk", e.getMessage());
            }

        }

    }

    public void subscribe(String topic, int qos) {
        try {
            client.subscribe(topic, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Logger.i("订阅成功");
//                    EventBus.getDefault().post(new Event.SubSuccess());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Logger.i("订阅失败" + exception.getMessage());
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic, int qos, IMqttActionListener listener) {
        try {

            client.subscribe(topic, qos, null, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe(String topic, Object usertext, IMqttActionListener listener) {
        try {

            client.unsubscribe(topic, usertext, listener);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void subscribe(String[] topic, int[] qos) {
        try {
            client.subscribe(topic, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Logger.i("订阅成功");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Logger.i("订阅失败" + exception.getMessage());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * 取消连接
     *
     * @throws MqttException
     */
    public void disConnect() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
            client = null;
        }
    }
}