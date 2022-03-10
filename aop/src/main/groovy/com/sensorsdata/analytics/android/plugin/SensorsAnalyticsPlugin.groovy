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

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle

/**
 * 插件类
 */
class SensorsAnalyticsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        Instantiator ins = ((DefaultGradle) project.getGradle()).getServices().get(
                Instantiator)
        def args = [ins] as Object[]
        //创建extension
        SensorsAnalyticsExtension extension = project.extensions.create("sensorsAnalytics", SensorsAnalyticsExtension,args)

        boolean disableSensorsAnalyticsPlugin = false//是否关闭神策分析插件
        boolean disableSensorsAnalyticsPluginNew = false
        boolean disableSensorsAnalyticsMultiThread = false
        boolean disableSensorsAnalyticsIncremental = false
        Properties properties = new Properties()
        //读取gradle设置
        if (project.rootProject.file('gradle.properties').exists()) {
            properties.load(project.rootProject.file('gradle.properties').newDataInputStream())
            disableSensorsAnalyticsPlugin = Boolean.parseBoolean(properties.getProperty("disableSensorsAnalyticsPlugin", "false"))
            disableSensorsAnalyticsPluginNew = Boolean.parseBoolean(properties.getProperty("sensorsAnalytics.disablePlugin", "false"))
            disableSensorsAnalyticsMultiThread = Boolean.parseBoolean(properties.getProperty("sensorsAnalytics.disableMultiThread", "false"))
            disableSensorsAnalyticsIncremental = Boolean.parseBoolean(properties.getProperty("sensorsAnalytics.disableIncremental", "false"))
        }
        if (!disableSensorsAnalyticsPlugin && !disableSensorsAnalyticsPluginNew) {
            AppExtension appExtension = project.extensions.findByType(AppExtension.class)
            //Transform帮助类
            SensorsAnalyticsTransformHelper transformHelper = new SensorsAnalyticsTransformHelper(extension)
            transformHelper.disableSensorsAnalyticsIncremental = disableSensorsAnalyticsIncremental
            transformHelper.disableSensorsAnalyticsMultiThread = disableSensorsAnalyticsMultiThread
            //注册Transform
            appExtension.registerTransform(new SensorsAnalyticsTransform(transformHelper))

            project.afterEvaluate {
                Logger.setDebug(extension.debug)
            }
        } else {
            Logger.error("------------您已关闭了神策插件--------------")
        }

    }
}