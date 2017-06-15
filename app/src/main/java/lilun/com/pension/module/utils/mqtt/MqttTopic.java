package lilun.com.pension.module.utils.mqtt;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.app.User;

/**
 * 订阅的主题
 *
 * @author yk
 *         create at 2017/6/7 15:21
 *         email : yk_developer@163.com
 */
public class MqttTopic {

    //普通求助
    public String topic_help_suffix = "/%23aid/.added";

    //个人私有
    public String personal_msg = "user/" + User.getUserName() + "/#";

    //公告
    public String topic_information_suffix = "/%23information/.added";


    //登陆
    public String login = "user/" + User.getUserName() + "/.login";


    //紧急求助
    public String urgent_help = "user/" + User.getUserName() + "/.help/10";


    public String[]  getAllTopicWhenInit(){
        List<String>  topics = new ArrayList<>();
        topics.add(personal_msg);
        ArrayList<String> levelIds = User.levelIds(false);
        for(String levelId:levelIds){
            String aidTopic = levelId +topic_help_suffix;
            String informationTopic = levelId+topic_information_suffix;
            topics.add(aidTopic);
            topics.add(informationTopic);
        }

        return topics.toArray(new String[topics.size()]);
    }


}
