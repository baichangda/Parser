package com.bcd.support_parser.impl.raco.data;

public enum FrameType {

    invalid(0x0000, "无效消息"),
    system_runtime_info(0x0001, "系统运行状态信息"),
    application_status_info(0x0002, "应用状态信息"),
    device_status_info(0x0003, "设备状态信息"),


    target_detect_info(0x0200, "目标检测信息"),
    lane_detect_info(0x0201, "车道检测信息"),
    detect_feature_data(0x0202, "检测特征数据"),


    cycle_statistics_info(0x0301, "周期统计信息"),
    area_statistics_info(0x0302, "区域统计信息"),
    trigger_statistics_info(0x0303, "触发统计信息"),
    queue_statistics_info(0x0304, "排队统计信息"),

    event_info(0x0500, "事件信息"),


    //日志信息 0x1000-0x10FF todo

    road_info(0x1100, "道路信息"),

    //原始数据 0x1200-0x12FF todo
    //配置参数信息 0x1300-0x1FFF todo
    //保留 0x2000-0xFFFF todo

    ;
    public final int type;
    public final String remark;

    FrameType(int type, String remark) {
        this.type = type;
        this.remark = remark;
    }
}
