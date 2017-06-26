package lilun.com.pensionlife.app;

import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.bean.OrganizationReply;
import lilun.com.pensionlife.module.bean.PushMessage;

/**
 * eventBus发送事件统一管理
 *
 * @author yk
 *         create at 2017/2/23 16:55
 *         email : yk_developer@163.com
 */

public class Event {
    //权限拒绝
    public static class PermissionDenied {
    }

    //token失效，跳转登录界面
    public static class TokenFailure {
    }

    //刷新邻居互助分类、列表页面数据
    public static class RefreshHelpData {
        public OrganizationAid aid;

        public RefreshHelpData setAid(OrganizationAid aid) {
            this.aid = aid;
            return this;
        }
    }

    //刷新互助详情页面
    public static class RefreshHelpDetail {
    }


    //刷新邻居互助回答列表
    public static class RefreshHelpReply {
        public OrganizationReply reply;

        public RefreshHelpReply(OrganizationReply reply) {
            this.reply = reply;
        }
    }

    //刷新我的订单列表数据
    public static class RefreshMyOrderData {
    }


    //刷新我的活动。活动列表数据
    public static class RefreshActivityData {
    }

    //刷新活动回复列表
    public static class RefreshActivityReply {
    }

    //刷新活动详情
    public static class RefreshActivityDetail {
    }


    //刷新消息推送栏
    public static class RefreshPushMessage {

    }

    //刷新消息推送栏
    public static class RefreshPushMessageChat {

    }

    //添加一个新的聊天消息
    public static class RefreshChatAddOne {
        PushMessage pushMessage;

        public RefreshChatAddOne(PushMessage pushMessage) {
            this.pushMessage = pushMessage;
        }

        public PushMessage getPushMessage() {
            return pushMessage;
        }
    }

    //强制退出活动
    public static class ForcedQuitChat {
        String showMessage;

        public ForcedQuitChat(String message) {
            showMessage = message;
        }

        public String getShowMessage() {
            return showMessage;
        }
    }


    //切换的当前组织
    public static class ChangedOrganization {
    }


    //刷新商家订单列表
    public static class RefreshMerchantOrder {
    }


    //刷新个人资料
    public static class RefreshContract {
    }


    //刷新紧急消息条数
    public static class RefreshUrgentInfo {

    }

    //刷新紧急消息条数
    public static class AnnounceH5 {
        public Information information;

        public AnnounceH5(Information information) {
            this.information = information;
        }
    }


    //登陆
    public static class OffLine {
    }

    //通知系统用户设置更改
    public static class AccountSettingChange {
    }


    //需要展示到app的消息
    public static class BoardMsg {
        public String data;
        public String topic;

        public BoardMsg(String topic, String data) {
            this.data = data;
            this.topic = topic;
        }
    }
}