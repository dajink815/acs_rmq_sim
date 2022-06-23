/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

package media.platform.acsrmqsim.rmq.types;

/**
 * Rabbit MQ Message Type
 *
 * @file RmqMessageType.java
 * @author Tony Lim
 */
public class RmqMessageType {
    //A2S-AMF
    public static final String RMQ_MSG_STR_SET_OFFER_REQ = "offer_req";
    public static final String RMQ_MSG_STR_SET_OFFER_RES = "offer_res";
    public static final String RMQ_MSG_STR_NEGO_REQ = "nego_req";
    public static final String RMQ_MSG_STR_NEGO_RES = "nego_res";
    public static final String RMQ_MSG_STR_HANGUP_REQ = "hangup_req";
    public static final String RMQ_MSG_STR_HANGUP_RES = "hangup_res";
    public static final String RMQ_MSG_STR_HEARTBEAT_REQ = "heartbeat_req";
    public static final String RMQ_MSG_STR_HEARTBEAT_RES = "heartbeat_res";
    public static final String RMQ_MSG_STR_LOGIN_REQ = "login_req";
    public static final String RMQ_MSG_STR_LOGIN_RES = "login_res";
    public static final String RMQ_MSG_STR_DEL_TASK_REQ = "del_task_req";
    public static final String RMQ_MSG_STR_DEL_TASK_RES = "del_task_res";
    public static final String RMQ_MSG_STR_EARLY_NEGO_REQ = "early_nego_req";
    public static final String RMQ_MSG_STR_EARLY_NEGO_RES = "early_nego_res";

    //A2S-AWF
    public static final String RMQ_MSG_STR_HB_REQ = "HB_REQ";
    public static final String RMQ_MSG_STR_HB_RES = "HB_RES";
    public static final String RMQ_MSG_STR_OUTCALL_START_REQ = "OUTCALL_START_REQ";
    public static final String RMQ_MSG_STR_OUTCALL_START_RES = "OUTCALL_START_RES";
    public static final String RMQ_MSG_STR_CALL_START_REQ = "CALL_START_REQ";
    public static final String RMQ_MSG_STR_CALL_START_RES = "CALL_START_RES";
    public static final String RMQ_MSG_STR_CALL_ADD_REQ = "CALL_ADD_REQ";
    public static final String RMQ_MSG_STR_CALL_ADD_RES = "CALL_ADD_RES";
    public static final String RMQ_MSG_STR_CALL_STOP_REQ = "CALL_STOP_REQ";
    public static final String RMQ_MSG_STR_CALL_STOP_RES = "CALL_STOP_RES";
    public static final String RMQ_MSG_STR_CALL_CLOSE_REQ = "CALL_CLOSE_REQ";
    public static final String RMQ_MSG_STR_CALL_CLOSE_RES = "CALL_CLOSE_RES";
    public static final String RMQ_MSG_STR_CALL_ABORT_REQ = "CALL_ABORT_REQ";
    public static final String RMQ_MSG_STR_CALL_ABORT_RES = "CALL_ABORT_RES";
    public static final String RMQ_MSG_STR_INCOMING_CALL_REQ = "INCOMING_CALL_REQ";
    public static final String RMQ_MSG_STR_INCOMING_CALL_RES = "INCOMING_CALL_RES";
    public static final String RMQ_MSG_STR_EARLY_MEDIA_REQ = "EARLYMEDIA_REQ";
    public static final String RMQ_MSG_STR_EARLY_MEDIA_RES = "EARLYMEDIA_RES";
    public static final String RMQ_MSG_STR_EARLY_MEDIA_DONE_REQ = "EARLYMEDIA_DONE_REQ";
    public static final String RMQ_MSG_STR_EARLY_MEDIA_DONE_RES = "EARLYMEDIA_DONE_RES";
    public static final String RMQ_MSG_STR_CALL_START_INFO_REQ = "CALL_START_INFO_REQ";
    public static final String RMQ_MSG_STR_CALL_START_INFO_RES = "CALL_START_INFO_RES";
    public static final String RMQ_MSG_STR_CALL_UPDATE_REQ = "CALL_UPDATE_REQ";
    public static final String RMQ_MSG_STR_CALL_UPDATE_RES = "CALL_UPDATE_RES";
    public static final String RMQ_MSG_STR_TASK_END_REQ = "TASK_END_REQ";
    public static final String RMQ_MSG_STR_TASK_END_RES = "TASK_END_RES";

    //AMF-AWF
    public static final String RMQ_MSG_STR_DTMF_START_REQ = "DTMF_START_REQ";
    public static final String RMQ_MSG_STR_DTMF_START_RES = "DTMF_START_RES";
    public static final String RMQ_MSG_STR_DTMF_RECV_REQ = "DTMF_RECV_REQ";
    public static final String RMQ_MSG_STR_DTMF_RECV_RES = "DTMF_RECV_RES";
    public static final String RMQ_MSG_STR_DTMF_SEND_REQ = "DTMF_SEND_REQ";
    public static final String RMQ_MSG_STR_DTMF_SEND_RES = "DTMF_SEND_RES";
    public static final String RMQ_MSG_STR_DTMF_STOP_REQ = "DTMF_STOP_REQ";
    public static final String RMQ_MSG_STR_DTMF_STOP_RES = "DTMF_STOP_RES";
    // send done?

    public static final String RMQ_MSG_STR_MEDIA_PLAY_REQ = "MEDIA_PLAY_REQ";
    public static final String RMQ_MSG_STR_MEDIA_PLAY_RES = "MEDIA_PLAY_RES";
    public static final String RMQ_MSG_STR_MEDIA_PLAY_DONE_REQ = "MEDIA_PLAY_DONE_REQ";
    public static final String RMQ_MSG_STR_MEDIA_PLAY_DONE_RES = "MEDIA_PLAY_DONE_RES";
    public static final String RMQ_MSG_STR_MEDIA_PLAY_STOP_REQ = "MEDIA_PLAY_STOP_REQ";
    public static final String RMQ_MSG_STR_MEDIA_PLAY_STOP_RES = "MEDIA_PLAY_STOP_RES";

    public static final String RMQ_MSG_STR_DIALOG_BEGIN_REQ = "DIALOG_BEGIN_REQ";
    public static final String RMQ_MSG_STR_DIALOG_BEGIN_RES = "DIALOG_BEGIN_RES";
    public static final String RMQ_MSG_STR_DIALOG_END_REQ = "DIALOG_END_REQ";
    public static final String RMQ_MSG_STR_DIALOG_END_RES = "DIALOG_END_RES";

    public static final String RMQ_MSG_STR_RTP_STOP_REQ = "RTP_STOP_REQ";
    public static final String RMQ_MSG_STR_RTP_STOP_RES = "RTP_STOP_RES";
    public static final String RMQ_MSG_STR_RTP_START_REQ = "RTP_START_REQ";
    public static final String RMQ_MSG_STR_RTP_START_RES = "RTP_START_RES";

    //A2S-AMF
    public static final int RMQ_MSG_TYPE_UNDEFINED = 0;
    public static final int RMQ_MSG_TYPE_SET_OFFER_REQ = 0x0001;
    public static final int RMQ_MSG_TYPE_SET_OFFER_RES = 0x1001;
    public static final int RMQ_MSG_TYPE_NEGO_REQ = 0x0002;
    public static final int RMQ_MSG_TYPE_NEGO_RES = 0x1002;
    public static final int RMQ_MSG_TYPE_HANGUP_REQ = 0x0003;
    public static final int RMQ_MSG_TYPE_HANGUP_RES = 0x1003;
    public static final int RMQ_MSG_TYPE_LOGIN_REQ = 0x0004;
    public static final int RMQ_MSG_TYPE_LOGIN_RES = 0x1004;
    public static final int RMQ_MSG_TYPE_HEARTBEAT_REQ = 0x0005;
    public static final int RMQ_MSG_TYPE_HEARTBEAT_RES = 0x1005;
    public static final int RMQ_MSG_TYPE_DEL_TASK_REQ = 0x0006;
    public static final int RMQ_MSG_TYPE_DEL_TASK_RES = 0x1006;
    public static final int RMQ_MSG_TYPE_EARLY_NEGO_REQ = 0x0007;
    public static final int RMQ_MSG_TYPE_EARLY_NEGO_RES = 0x1008;

    //A2S-AWF
    public static final int RMQ_MSG_TYPE_HB_REQ = 0x0011;
    public static final int RMQ_MSG_TYPE_HB_RES = 0x1011;
    public static final int RMQ_MSG_TYPE_OUTCALL_START_REQ = 0x0012;
    public static final int RMQ_MSG_TYPE_OUTCALL_START_RES = 0x1012;
    public static final int RMQ_MSG_TYPE_CALL_START_REQ = 0x0013;
    public static final int RMQ_MSG_TYPE_CALL_START_RES = 0x1013;
    public static final int RMQ_MSG_TYPE_CALL_ADD_REQ = 0x0014;
    public static final int RMQ_MSG_TYPE_CALL_ADD_RES = 0x1014;
    public static final int RMQ_MSG_TYPE_CALL_STOP_REQ = 0x0015;
    public static final int RMQ_MSG_TYPE_CALL_STOP_RES = 0x1015;
    public static final int RMQ_MSG_TYPE_CALL_CLOSE_REQ = 0x0016;
    public static final int RMQ_MSG_TYPE_CALL_CLOSE_RES = 0x1016;
    public static final int RMQ_MSG_TYPE_CALL_ABORT_REQ = 0x0017;
    public static final int RMQ_MSG_TYPE_CALL_ABORT_RES = 0x1017;
    public static final int RMQ_MSG_TYPE_INCOMING_CALL_REQ = 0x0018;
    public static final int RMQ_MSG_TYPE_INCOMING_CALL_RES = 0x1018;
    public static final int RMQ_MSG_TYPE_EARLY_MEDIA_REQ = 0x0019;
    public static final int RMQ_MSG_TYPE_EARLY_MEDIA_RES = 0x1019;
    public static final int RMQ_MSG_TYPE_EARLY_MEDIA_DONE_REQ = 0x0021;
    public static final int RMQ_MSG_TYPE_EARLY_MEDIA_DONE_RES = 0x1021;
    public static final int RMQ_MSG_TYPE_CALL_START_INFO_REQ = 0x0022;
    public static final int RMQ_MSG_TYPE_CALL_START_INFO_RES = 0x1022;
    public static final int RMQ_MSG_TYPE_CALL_UPDATE_REQ = 0x0023;
    public static final int RMQ_MSG_TYPE_CALL_UPDATE_RES = 0x1023;
    public static final int RMQ_MSG_TYPE_TASK_END_REQ = 0x0024;
    public static final int RMQ_MSG_TYPE_TASK_END_RES = 0x1024;


    //AMF-AWF
    public static final int RMQ_MSG_TYPE_DTMF_START_REQ = 0x0031;
    public static final int RMQ_MSG_TYPE_DTMF_START_RES = 0x1031;
    public static final int RMQ_MSG_TYPE_DTMF_RECV_REQ = 0x0032;
    public static final int RMQ_MSG_TYPE_DTMF_RECV_RES = 0x1032;
    public static final int RMQ_MSG_TYPE_DTMF_SEND_REQ = 0x0033;
    public static final int RMQ_MSG_TYPE_DTMF_SEND_RES = 0x1033;
    public static final int RMQ_MSG_TYPE_DTMF_STOP_REQ = 0x0034;
    public static final int RMQ_MSG_TYPE_DTMF_STOP_RES = 0x1034;
    // send done?

    public static final int RMQ_MSG_TYPE_MEDIA_PLAY_REQ = 0x0041;
    public static final int RMQ_MSG_TYPE_MEDIA_PLAY_RES = 0x1041;
    public static final int RMQ_MSG_TYPE_MEDIA_PLAY_DONE_REQ = 0x0042;
    public static final int RMQ_MSG_TYPE_MEDIA_PLAY_DONE_RES = 0x1042;
    public static final int RMQ_MSG_TYPE_MEDIA_PLAY_STOP_REQ = 0x0043;
    public static final int RMQ_MSG_TYPE_MEDIA_PLAY_STOP_RES = 0x1043;

    public static final int RMQ_MSG_TYPE_DIALOG_BEGIN_REQ = 0x0044;
    public static final int RMQ_MSG_TYPE_DIALOG_BEGIN_RES = 0x1044;
    public static final int RMQ_MSG_TYPE_DIALOG_END_REQ = 0x0045;
    public static final int RMQ_MSG_TYPE_DIALOG_END_RES = 0x1045;

    public static final int RMQ_MSG_TYPE_RTP_STOP_REQ = 0x0046;
    public static final int RMQ_MSG_TYPE_RTP_STOP_RES = 0x1046;
    public static final int RMQ_MSG_TYPE_RTP_START_REQ = 0x0047;
    public static final int RMQ_MSG_TYPE_RTP_START_RES = 0x1047;

    public static final int RMQ_MSG_COMMON_REASON_CODE_SUCCESS = 0;
    public static final int RMQ_MSG_COMMON_REASON_CODE_FAILURE = -1;
    public static final int RMQ_MSG_COMMON_REASON_CODE_WRONG_PARAM = -3;

    public static final int RMQ_MSG_COMMON_REASON_CODE_ALREADY_CLOSE = -4;

    public static final int RMQ_MSG_COMMON_REASON_CODE_NO_SESSION = 2000;
    public static final int RMQ_MSG_COMMON_REASON_CODE_LICENSE_OVER = 3005;

/*
    private static Map<String, Integer> typeMap() {
        return Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_SET_OFFER_REQ, RMQ_MSG_TYPE_SET_OFFER_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_SET_OFFER_RES, RMQ_MSG_TYPE_SET_OFFER_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_EARLY_NEGO_REQ, RMQ_MSG_TYPE_EARLY_NEGO_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_EARLY_NEGO_RES, RMQ_MSG_TYPE_EARLY_NEGO_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_NEGO_REQ, RMQ_MSG_TYPE_NEGO_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_NEGO_RES, RMQ_MSG_TYPE_NEGO_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_HANGUP_REQ, RMQ_MSG_TYPE_HANGUP_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_HANGUP_RES, RMQ_MSG_TYPE_HANGUP_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_LOGIN_REQ, RMQ_MSG_TYPE_LOGIN_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_LOGIN_RES, RMQ_MSG_TYPE_LOGIN_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_HEARTBEAT_REQ, RMQ_MSG_TYPE_HEARTBEAT_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_HEARTBEAT_RES, RMQ_MSG_TYPE_HEARTBEAT_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DEL_TASK_REQ, RMQ_MSG_TYPE_DEL_TASK_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DEL_TASK_RES, RMQ_MSG_TYPE_DEL_TASK_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_HB_REQ, RMQ_MSG_TYPE_HB_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_HB_RES, RMQ_MSG_TYPE_HB_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_OUTCALL_START_REQ, RMQ_MSG_TYPE_OUTCALL_START_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_OUTCALL_START_RES, RMQ_MSG_TYPE_OUTCALL_START_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_START_REQ, RMQ_MSG_TYPE_CALL_START_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_START_RES, RMQ_MSG_TYPE_CALL_START_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_ADD_REQ, RMQ_MSG_TYPE_CALL_ADD_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_ADD_RES, RMQ_MSG_TYPE_CALL_ADD_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_STOP_REQ, RMQ_MSG_TYPE_CALL_STOP_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_STOP_RES, RMQ_MSG_TYPE_CALL_STOP_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_CLOSE_REQ, RMQ_MSG_TYPE_CALL_CLOSE_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_CLOSE_RES, RMQ_MSG_TYPE_CALL_CLOSE_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_ABORT_REQ, RMQ_MSG_TYPE_CALL_ABORT_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_ABORT_RES, RMQ_MSG_TYPE_CALL_ABORT_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_INCOMING_CALL_REQ, RMQ_MSG_TYPE_INCOMING_CALL_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_INCOMING_CALL_RES, RMQ_MSG_TYPE_INCOMING_CALL_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_EARLY_MEDIA_REQ, RMQ_MSG_TYPE_EARLY_MEDIA_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_EARLY_MEDIA_RES, RMQ_MSG_TYPE_EARLY_MEDIA_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_EARLY_MEDIA_DONE_REQ, RMQ_MSG_TYPE_EARLY_MEDIA_DONE_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_EARLY_MEDIA_DONE_RES, RMQ_MSG_TYPE_EARLY_MEDIA_DONE_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_START_INFO_REQ, RMQ_MSG_TYPE_CALL_START_INFO_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_START_INFO_RES, RMQ_MSG_TYPE_CALL_START_INFO_RES),

                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DTMF_START_REQ, RMQ_MSG_TYPE_DTMF_START_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DTMF_START_RES, RMQ_MSG_TYPE_DTMF_START_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DTMF_RECV_REQ, RMQ_MSG_TYPE_DTMF_RECV_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DTMF_RECV_RES, RMQ_MSG_TYPE_DTMF_RECV_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DTMF_SEND_REQ, RMQ_MSG_TYPE_DTMF_SEND_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DTMF_SEND_RES, RMQ_MSG_TYPE_DTMF_SEND_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DTMF_STOP_REQ, RMQ_MSG_TYPE_DTMF_STOP_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DTMF_STOP_RES, RMQ_MSG_TYPE_DTMF_STOP_RES),

                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_MEDIA_PLAY_REQ, RMQ_MSG_TYPE_MEDIA_PLAY_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_MEDIA_PLAY_RES, RMQ_MSG_TYPE_MEDIA_PLAY_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_MEDIA_PLAY_DONE_REQ, RMQ_MSG_TYPE_MEDIA_PLAY_DONE_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_MEDIA_PLAY_DONE_RES, RMQ_MSG_TYPE_MEDIA_PLAY_DONE_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_MEDIA_PLAY_STOP_REQ, RMQ_MSG_TYPE_MEDIA_PLAY_STOP_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_MEDIA_PLAY_STOP_RES, RMQ_MSG_TYPE_MEDIA_PLAY_STOP_RES),

                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DIALOG_BEGIN_REQ, RMQ_MSG_TYPE_DIALOG_BEGIN_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DIALOG_BEGIN_RES, RMQ_MSG_TYPE_DIALOG_BEGIN_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DIALOG_END_REQ, RMQ_MSG_TYPE_DIALOG_END_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_DIALOG_END_RES, RMQ_MSG_TYPE_DIALOG_END_RES),

                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_UPDATE_REQ, RMQ_MSG_TYPE_CALL_UPDATE_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_CALL_UPDATE_RES, RMQ_MSG_TYPE_CALL_UPDATE_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_TASK_END_REQ, RMQ_MSG_TYPE_TASK_END_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_TASK_END_RES, RMQ_MSG_TYPE_TASK_END_RES),

                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_RTP_STOP_REQ, RMQ_MSG_TYPE_RTP_STOP_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_RTP_STOP_RES, RMQ_MSG_TYPE_RTP_STOP_RES),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_RTP_START_REQ, RMQ_MSG_TYPE_RTP_START_REQ),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_STR_RTP_START_RES, RMQ_MSG_TYPE_RTP_START_RES)

        ).collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));
    }

    // 규격에 맞춰 수정 필요
    private static Map<Integer, String> typeStrMap() {
        return Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_SET_OFFER_REQ, "SetOfferReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_SET_OFFER_RES, "SetOfferRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_EARLY_NEGO_REQ, "SetEarlyNegoReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_EARLY_NEGO_RES, "SetEarlyNegoRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_NEGO_REQ, "SetNegoDoneReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_NEGO_RES, "SetNegoDoneRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_HANGUP_REQ, "SetHangupReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_HANGUP_RES, "SetHangupRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_LOGIN_REQ, "SetLogInReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_LOGIN_RES, "SetLogInRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_DEL_TASK_REQ, "SetDelTaskReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_DEL_TASK_RES, "SetDelTaskRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_HEARTBEAT_REQ, "SetHeartbeatReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_HEARTBEAT_RES, "SetHeartbeatRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_HB_REQ, "SetHBReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_HB_RES, "SetHBRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_OUTCALL_START_REQ, "SetOutCallStartReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_OUTCALL_START_RES, "SetOutCallStartReS"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_START_REQ, "SetCallStartReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_START_RES, "SetCallStartRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_ADD_REQ, "SetCallAddReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_ADD_RES, "SetCallAddRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_STOP_REQ, "SetCallStopReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_STOP_RES, "SetCallStopRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_CLOSE_REQ, "SetCallCloseReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_CLOSE_RES, "SetCallCloseRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_ABORT_REQ, "SetCallAbortReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_ABORT_RES, "SetCallAbortRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_INCOMING_CALL_REQ, "SetIncomingCallReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_INCOMING_CALL_RES, "SetIncomingCallRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_EARLY_MEDIA_REQ, "SetEarlyMediaReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_EARLY_MEDIA_RES, "SetEarlyMediaRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_EARLY_MEDIA_DONE_REQ, "SetEarlyMediaDoneReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_EARLY_MEDIA_DONE_RES, "SetEarlyMediaDoneRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_START_INFO_REQ, "SetCallStartInfoReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_START_INFO_RES, "SetCallStartInfoRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_UPDATE_REQ, "SetCallUpdateReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_CALL_UPDATE_RES, "SetCallUpdateRes"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_TASK_END_REQ, "SetTaskEndReq"),
                new AbstractMap.SimpleEntry<>(RMQ_MSG_TYPE_TASK_END_RES, "SetTaskEndRes")

                // 필요하면 메시지 타입 추가
        ).collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));
    }

    public static int getMessageType(String typeStr) {
        Integer value = typeMap().get(typeStr);
        return (value == null) ? RMQ_MSG_TYPE_UNDEFINED : value.intValue();
    }

    public static String getMessageTypeStr(int messageType) {
        return typeStrMap().get(messageType);
    }*/
}