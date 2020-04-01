package com.fenghuang.caipiaobao.widget.videoplayer.assist;

public interface DataInter {

    interface Event extends InterEvent {

        int EVENT_CODE_REQUEST_BACK = -100;
        int EVENT_CODE_REQUEST_CLOSE = -101;
        int EVENT_CODE_REQUEST_CLOSE_ENTER = -109;
        int EVENT_CODE_REQUEST_TOGGLE_SCREEN = -104;
        int EVENT_CODE_REQUEST_NEXT = -106;
        int EVENT_CODE_ERROR_SHOW = -111;

        int EVENT_CODE_BOTTOM_GIFT = -108;

        int EVENT_CODE_BOTTOM_RED = -110;

        int EVENT_CODE_ATTENTION = -112;

        int EVENT_CODE_REDTIPS = -113;
        int EVENT_CODE_DAM_MU = -114; //发弹幕
        int EVENT_CODE_DAM_MU_SWITCH = -115;//弹幕开关
        int EVENT_CODE_RECHARGE = -116;//充值
    }

    interface Key extends InterKey {

        String KEY_IS_LANDSCAPE = "isLandscape";

        String KEY_DATA_SOURCE = "data_source";

        String KEY_ERROR_SHOW = "error_show";

        String KEY_IS_HAS_NEXT = "is_has_next";
        String KEY_COMPLETE_SHOW = "complete_show";
        String KEY_CONTROLLER_TOP_ENABLE = "controller_top_enable";
        String KEY_CONTROLLER_SCREEN_SWITCH_ENABLE = "screen_switch_enable";

        String KEY_TIMER_UPDATE_ENABLE = "timer_update_enable";

        String KEY_NETWORK_RESOURCE = "network_resource";

    }

    interface ReceiverKey {
        String KEY_LOADING_COVER = "loading_cover";
        String KEY_CONTROLLER_COVER = "controller_cover";
        String KEY_CONTROLLER_live_COVER = "controller_live_cover";
        String KEY_GESTURE_COVER = "gesture_cover";
        String KEY_COMPLETE_COVER = "complete_cover";
        String KEY_ERROR_COVER = "error_cover";
        String KEY_CLOSE_COVER = "close_cover";
        String KEY_STATE_COVER = "state_cover";
    }

    interface PrivateEvent {
        int EVENT_CODE_UPDATE_SEEK = -201;
    }

}
