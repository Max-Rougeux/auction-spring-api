package com.rougeux.auction.support;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiCodes {

    public final String CD_BAD_REQUEST = "700" ;
    public final String CD_UNAUTHENTICATED = "701";
    public final String CD_ERR_UNAUTHORIZED = "703" ;
    public final String CD_ERR_FORBIDDEN = "703" ;
    public final String CD_NOT_FOUND = "704";
    public final String CD_INTERNAL_ERROR = "705";

    public final String CD_OK_USER_AUTHENTICATED = "201";
    public final String CD_OK_USER_LOGOUT = "205";
    public final String CD_OK_USER_REFRESH = "203";
    public final String CD_DATA_SUCCESS = "202";

    public final String CD_OK_BID_SUBMIT = "209";

    public final String CD_ERR_BID_SALE_CLOSED = "720";
    public final String CD_ERR_BID_AMOUNT_LOW = "721";
    public final String CD_ERR_BID_ALREADY_TOP = "722";
    public final String CD_ERR_BID_CREDIT_LOW = "723";
    public final String CD_ERR_BID_SALE_OWNER = "724";
}
