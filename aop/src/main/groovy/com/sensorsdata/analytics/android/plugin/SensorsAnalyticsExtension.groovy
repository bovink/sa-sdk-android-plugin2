/*
 * Created by wangzhuozhou on 2015/08/12.
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

import org.gradle.api.Action
import org.gradle.internal.reflect.Instantiator


/**
 * 神策分析扩展
 * 相关变量可以在gradle中设置
 */
class SensorsAnalyticsExtension {
    boolean debug = false
    boolean disableJar = false
    boolean useInclude = false
    boolean lambdaEnabled = true

    ArrayList<String> exclude = []
    ArrayList<String> include = []

    SensorsAnalyticsSDKExtension sdk

    SensorsAnalyticsExtension(Instantiator ins) {
        sdk = ins.newInstance(SensorsAnalyticsSDKExtension)
    }

    void sdk(Action<? super SensorsAnalyticsSDKExtension> action) {
        action.execute(sdk)
    }

    @Override
    String toString() {
        StringBuilder excludeBuilder = new StringBuilder()
        int length = exclude.size()
        for (int i = 0; i < length; i++) {
            excludeBuilder.append("'").append(exclude.get(i)).append("'")
            if (i != length - 1) {
                excludeBuilder.append(",")
            }
        }

        StringBuilder includeBuilder = new StringBuilder()
        length = include.size()
        for (int i = 0; i < length; i++) {
            includeBuilder.append("'").append(include.get(i)).append("'")
            if (i != length - 1) {
                includeBuilder.append(",")
            }
        }
        return  "\tdebug=" + debug + "\n" +
                "\tdisableJar=" + disableJar + "\n" +
                "\tuseInclude=" + useInclude + "\n" +
                "\tlambdaEnabled=" + lambdaEnabled + "\n" +
                "\texclude=[" + excludeBuilder.toString() +"]"+ "\n" +
                "\tinclude=[" + includeBuilder.toString() +"]"+ "\n" +
                "\tsdk {\n" + sdk + "\n"+
                "\t}"
    }
}

