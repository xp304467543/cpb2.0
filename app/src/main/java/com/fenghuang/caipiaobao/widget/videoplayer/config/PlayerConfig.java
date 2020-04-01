package com.fenghuang.caipiaobao.widget.videoplayer.config;


import androidx.collection.SparseArrayCompat;

import com.fenghuang.caipiaobao.widget.videoplayer.AlivcPlayer.AlivcPlayer;
import com.fenghuang.caipiaobao.widget.videoplayer.entity.DecoderPlan;


/**
 * Created by Taurus on 2018/3/17.
 *
 * The configuration of the player is used for
 * the management of the decoder scheme.
 * You can add more than one decoding scheme.
 *
 */

public class PlayerConfig {

    public static final int DEFAULT_PLAN_ID = 0;

    //default decoder plan id is use System MediaPlayer.
    private static int defaultPlanId = DEFAULT_PLAN_ID;

    //decoder plans arrays.
    private static SparseArrayCompat<DecoderPlan> mPlans;

    //Whether or not use the default NetworkEventProducer.
    //default state false.
    private static boolean useDefaultNetworkEventProducer = false;

    static {
        mPlans = new SparseArrayCompat<>(2);

        //add default plan
        DecoderPlan defaultPlan = new DecoderPlan(DEFAULT_PLAN_ID, AlivcPlayer.class.getName(),"AlivcPlayer");
        addDecoderPlan(defaultPlan);
        //set default plan id
        setDefaultPlanId(DEFAULT_PLAN_ID);
    }

    public static void addDecoderPlan(DecoderPlan plan){
        mPlans.put(plan.getIdNumber(), plan);
    }

    /**
     * setting default DecoderPlanId.
     * @param planId
     */
    public static void setDefaultPlanId(int planId){
        defaultPlanId = planId;
    }

    /**
     * get current DecoderPlanId.
     * @return
     */
    public static int getDefaultPlanId(){
        return defaultPlanId;
    }

    public static DecoderPlan getDefaultPlan(){
        return getPlan(defaultPlanId);
    }

    public static DecoderPlan getPlan(int planId){
        return mPlans.get(planId);
    }

    /**
     * Judging the legality of planId.
     * @param planId
     * @return
     */
    public static boolean isLegalPlanId(int planId){
        DecoderPlan plan = getPlan(planId);
        return plan!=null;
    }

    //if you want to use default NetworkEventProducer, set it true.
    public static void setUseDefaultNetworkEventProducer(boolean useDefaultNetworkEventProducer) {
        PlayerConfig.useDefaultNetworkEventProducer = useDefaultNetworkEventProducer;
    }

    public static boolean isUseDefaultNetworkEventProducer() {
        return useDefaultNetworkEventProducer;
    }
}
