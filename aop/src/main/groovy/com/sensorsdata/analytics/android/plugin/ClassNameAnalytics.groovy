/*
 * Created by renqingyou on 2018/12/01.
 * Copyright 2015－2019 Sensors Data Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sensorsdata.analytics.android.plugin

class ClassNameAnalytics {

    public String className

    //是否需要字节码插桩处理
    boolean isShouldModify = false

    boolean isSensorsDataAPI = false

    boolean isSensorsDataUtils = false

    boolean isSALog = false

    /**
     * 当前类需要禁用的列表
     */
    def methodCells = new ArrayList<SensorsAnalyticsMethodCell>()

    ClassNameAnalytics (String className) {
        this.className = className
        //判断类是否是SensorsDataAPI
        isSensorsDataAPI = (className == 'com.sensorsdata.analytics.android.sdk.SensorsDataAPI')
        isSensorsDataUtils = (className == 'com.sensorsdata.analytics.android.sdk.util.SensorsDataUtils')
        isSALog = (className == 'com.sensorsdata.analytics.android.sdk.SALog')
    }

    /**
     * 是否是SDK中的文件
     * @return
     */
    boolean isSDKFile() {
        return isSALog || isSensorsDataAPI || isSensorsDataUtils
    }

    boolean isLeanback() {
        return className.startsWith("android.support.v17.leanback") || className.startsWith("androidx.leanback")
    }

    /**
     * Android自动生成的类
     * @return
     */
    boolean isAndroidGenerated() {
        return className.contains('R$') ||
                className.contains('R2$') ||
                className.contains('R.class') ||
                className.contains('R2.class') ||
                className.contains('BuildConfig.class')
    }

}