package com.ifeimo.im.framwork.message;

/**
 * Created by lpds on 2017/6/16.
 */
public interface PDF {

    /**
     *
     *
     *
     * ------------ IM connect code
     *
     * Code XMPP Error Type
     * 500 interna-server-error WAIT
     * 403 forbidden AUTH
     * 400bad-request MODIFY
     * 404 item-not-found CANCEL
     * 409 conflict CANCEL
     * 501 feature-not-implemented CANCEL
     * 302 gone MODIFY
     * 400 jid-malformed MODIFY
     * 406 no-acceptable MODIFY
     * 405 not-allowed CANCEL
     * 401 not-authorized AUTH
     * 402 payment-required AUTH
     * 404 recipient-unavailable WAIT
     * 302 redirect MODIFY
     * 407 registration-required AUTH
     * 404 remote-server-not-found CANCEL
     * 504 remote-server-timeout WAIT
     * 502 remote-server-error CANCEL
     * 500 resource-constraint WAIT
     * 503 service-unavailable CANCEL
     * 407 subscription-required AUTH
     * 500 undefined-condition WAIT
     * 400 unexpected-condition WAIT
     * 408 request-timeout CANCEL
     */


    /**
     *
     *
     * --------------------- prsences
     *
     * 100

     告知用户任何人都可以看到用户的完整的JID

     101

     通知用户,他或她的关系改变而不是在房间里

     103

     通知住户房间现在显示未上线用户

     104

     通知住户房间现在只显示上线用户

     110

     通知用户presence包也给自己发

     170

     通知房间用户现在房间允许进入

     171

     通知房间用户现在房间不允许进入

     172

     通知房间用户房间非匿名

     173

     通知房间用户房间匿名

     201

     通知用户创建了一个新房间

     210

     通知用户房间分配或修改了某个人的昵称

     301

     通知用户他被这个房间拉入黑名单

     303

     通知所有用户房间被修改了新昵称

     307

     通知用户,他或她已被踢出了房间

     321

     通知用户,他或她是要离开房间，因为关系的改变

     322

     通知用户，他离开这这个房间 因为房间只允许会员进入 而他不是会员

     332

     通知用户他离开了这个房间 因为MUC服务被关闭
     */
}
