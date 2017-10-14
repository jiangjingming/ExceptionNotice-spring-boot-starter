package com.mhc.ambulance.consts.enums;

/**
 * Created by zhenmo on 16/9/22.
 */
public enum ErrorCodeEnum {

    /*
     * 错误码结构: FPC+三位业务类别+三位错误码<br>
     * 错误代码解决：DTB
     * ===========================================<br> 100表示系统类别<br>
     * 101表示通用类别<br>
     */

    /*--------- 100开头表示系统级别错误 ---------*/
    SYS_ERROR("FPC100001", "系统内部错误"),

    /*--------- 101开头表示通用类别 ---------*/
    SUCCESS("FPC101001", "成功"),
    FAILURE("FPC101002", "失败"),
    PARAM_ERROR("FPC101003", "参数错误"),
    PARAM_EMPTY("FPC101004", "参数为空"),
    NO_MODIFY("FPC201005", "您尚未修改任何信息，内容无需更新"),
    NO_DATA("FPC2010006", "无数据"),
    INSERT_FAIL("FPC201007", "插入数据错误"),
    QUERY_FAIL("FPC201008", "查询错误"),
    UPDATE_FAIL("FPC201009", "修改数据错误"),
    DATA_ERROR("FPC201010", "数据异常"),

    /*--------- 201开头表示预警规则类错误 ---------*/
    RISK_RULE_IS_NOT_DISABLE("FPC201001","已禁用"),
    RISK_RULE_IS_NOT_ENABLE("FPC201001","已开启"),
    NOT_EXIST_OR_DISABLE_RISK_RULE("FPC201002","该预警规则不存在或已禁用"),
    /*--------- 210开头表示外部系统错误 ---------*/
    // 2101开头表示经销商（客户）类的错误
    DEALER_NOT_EXISTS("FPC210101", "客户不存在"),
    DEALER_AREA_NOT_EXISTS("FPC210102", "客户地区信息不存在"),


    /*--------- 301开头表示数据库CRUD失败 ---------*/
    INSERT_FAILD("BTB30101", "添加数据失败"),
    UPDATE_FAILD("BTB30102", "修改数据失败"),
    DELETE_FAILD("BTB30103", "删除数据失败"),
    ;

    private String errMsg;
    private String errCode;

    ErrorCodeEnum(String errCode, String errMsg) {
        this.errMsg = errMsg;
        this.errCode = errCode;
    }

    /**
     * 根据类型值获取相应的枚举类
     *
     * @param code
     * @return
     */
    public static ErrorCodeEnum getErrorCodeEnum(String code) {
        for (ErrorCodeEnum c : ErrorCodeEnum.values()) {
            if (c.getErrCode().equals(code)) {
                return c;
            }
        }

        return null;
    }

    public String toString() {
        return errCode + ": " + errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
