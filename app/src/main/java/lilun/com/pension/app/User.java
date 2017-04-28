package lilun.com.pension.app;

import android.text.TextUtils;

import java.util.List;

import lilun.com.pension.module.bean.OrganizationAccount;
import lilun.com.pension.module.utils.ACache;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.StringUtils;

/**
 * 关于用户的常用操作
 *
 * @author yk
 *         create at 2017/2/13 13:32
 *         email : yk_developer@163.com
 */
public class User {
    public static final String token = "token";
    public static final String userId = "userId";
    public static final String name = "name";
    public static final String username = "username";
    public static final String password = "password";
    public static final String mobile = "mobile";
    public static final String isCustomer = "isCustomer";
    public static final String defaultContactId = "defaultContactId ";
    public static final String rootOrganizationAccountId = "rootOrganizationAccountId";
    public static final String belongOrganizationAccountId = "belongOrganizationAccountId";
    public static final String currentOrganizationAccountId = "currentOrganizationAccountId";
    public static final String defOrganizationId = "/";
    public static final String belongsOrganizationId = "belongsOrganizationId";
    public static final String currentOrganizationId = "currentOrganizationId";
    public static final String belongOrganizations = "belongOrganizations";


    public static String getUserId() {return PreUtils.getString(userId, "");}
    public static void putUserId(String userId) {
        PreUtils.putString(User.userId, userId);
    }




    public static String getUserName() {return PreUtils.getString(username, "");}
    public static void putUserName(String un) {PreUtils.putString(username, un);}




    public static String getPassword() {return PreUtils.getString(password, "");}
    public static void putPassword(String pass) {PreUtils.putString(password, pass);}



    public static String getToken() {
        return PreUtils.getString(token, "");
    }




    public static String getName() {
        return PreUtils.getString(name, "");
    }
    public static void putName(String nam) {
        PreUtils.putString(name, nam);
    }




    public static void putIsCustomer(boolean isCustomer) {PreUtils.putBoolean(User.isCustomer, isCustomer);}
    public static boolean isCustomer() {
        return PreUtils.getBoolean(User.isCustomer, true);
    }



    public static String getCurrentOrganizationId() {return PreUtils.getString(currentOrganizationId, defOrganizationId);}
    public static void putCurrentOrganizationId(String id) {PreUtils.putString(currentOrganizationId, TextUtils.isEmpty(id) ? User.defOrganizationId : id);}




    public static String getBelongsOrganizationId() {return PreUtils.getString(belongsOrganizationId, defOrganizationId);}
    public static void putBelongsOrganizationId(String id) { PreUtils.putString(belongsOrganizationId, TextUtils.isEmpty(id) ? User.defOrganizationId : id);}


    public static String getCurrentOrganizationName() {
        return StringUtils.getOrganizationNameFromId(PreUtils.getString(currentOrganizationId, defOrganizationId));
    }


    public static String getBelongsOrganizationName() {
        return StringUtils.getOrganizationNameFromId(PreUtils.getString(belongsOrganizationId, defOrganizationId));
    }




    /**
    *创建者是否是自己
    */
    public static boolean creatorIsOwn(String creatorId) {
        return getUserId().equals(creatorId);
    }


    /**
     *获取当前组织是否已经切换
     */
    public static boolean currentOrganizationHasChanged() {
        return  PreUtils.getBoolean("currentOrganizationHadChanged", false);
    }

    /**
     *存当前组织是否已经切换
     */
    public static boolean putCurrentOrganizationHasChanged(boolean changed) {
        return  PreUtils.putBoolean("currentOrganizationHadChanged", changed);
    }



    /**
    *获取用户的组织账号
    */
    public static List<OrganizationAccount> getBelongOrganization() {
        if (ACache.get().getAsObject(belongOrganizations) != null) {
            return (List<OrganizationAccount>) ACache.get().getAsObject(belongOrganizations);
        }
        return null;
    }


    //默认资料
    public static void putContactId(String defaultContactId) {
        PreUtils.putString(defaultContactId, defaultContactId);
    }

    public static String getContactId() {
        return PreUtils.getString(defaultContactId, "");
    }



    //地球村OrganizationAccount  的id
    public static void putRootOrganizationAccountId(String rootId) {
        PreUtils.putString(rootOrganizationAccountId, rootId);
    }

    public static String getRootOrganizationAccountId() {
        return PreUtils.getString(rootOrganizationAccountId, "");
    }


    //默认所属组织OrganizationAccount  的id
    public static void putBelongOrganizationAccountId(String rootId) {
        PreUtils.putString(belongOrganizationAccountId, rootId);
    }

    public static String getBelongOrganizationAccountId() {
        return PreUtils.getString(belongOrganizationAccountId, "");
    }



    //当前所属组织OrganizationAccount  的id
    public static void putCurrentOrganizationAccountId(String rootId) {
        PreUtils.putString(currentOrganizationAccountId, rootId);
    }

    public static String getCurrentOrganizationAccountId() {
        return PreUtils.getString(currentOrganizationAccountId, "");
    }



    //电话
    public static void putMobile(String phone) {
        PreUtils.putString(mobile, phone);
    }

    public static String getMobile() {
        return PreUtils.getString(mobile, "");
    }

}



