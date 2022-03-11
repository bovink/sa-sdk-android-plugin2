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

class SensorsAnalyticsTransformHelper {

    SensorsAnalyticsExtension extension

    SensorsAnalyticsSDKHookConfig sensorsAnalyticsHookConfig

    boolean disableSensorsAnalyticsMultiThread

    boolean disableSensorsAnalyticsIncremental

    HashSet<String> exclude = ['com.sensorsdata.analytics.android.sdk', 'android.support', 'androidx','com.qiyukf', 'android.arch']
    HashSet<String> include = ['butterknife.internal.DebouncingOnClickListener',
                                              'com.jakewharton.rxbinding.view.ViewClickOnSubscribe',
                                              'com.facebook.react.uimanager.NativeViewHierarchyManager']

    SensorsAnalyticsTransformHelper(SensorsAnalyticsExtension extension) {
        this.extension = extension
    }

    /**
     * helper添加exclude类和include类
     * 设置钩子配置
     */
    void onTransform() {
        //打印扩展详情
        println("sensorsAnalytics {\n"+extension+"\n}")
        //添加扩展中设置的include和exclude
        ArrayList<String> excludePackages = extension.exclude
        if (excludePackages != null) {
            exclude.addAll(excludePackages)
        }
        ArrayList<String> includePackages = extension.include
        if (includePackages != null) {
            include.addAll(includePackages)
        }

        createSensorsAnalyticsHookConfig()
    }

    /**
     * 根据SensorsAnalyticsSDKExtension在Gradle中设置的属性调用Config方法
     */
    private void createSensorsAnalyticsHookConfig() {
        sensorsAnalyticsHookConfig = new SensorsAnalyticsSDKHookConfig()
        //遍历SensorsAnalyticsSDKExtension的属性
        List<MetaProperty> metaProperties = SensorsAnalyticsSDKExtension.getMetaClass().properties
        for (it in metaProperties) {
            if (it.name == 'class') {
                continue
            }
            //根据设置同步调用Config里的方法
            if (extension.sdk."${it.name}") {
                sensorsAnalyticsHookConfig."${it.name}"(it.name)
            }
        }
    }

    /**
     * 根据类名进行分析
     * @param className
     * @return
     */
    ClassNameAnalytics analytics(String className) {

        ClassNameAnalytics classNameAnalytics = new ClassNameAnalytics(className)

        if (classNameAnalytics.isSDKFile()) {
            def cellHashMap = sensorsAnalyticsHookConfig.methodCells
            cellHashMap.each {
                key,value->
                    def methodCellList = value.get(className.replace('.','/'))
                    if (methodCellList != null) {
                        classNameAnalytics.methodCells.addAll(methodCellList)
                    }

            }
            if (classNameAnalytics.methodCells.size() > 0 || classNameAnalytics.isSensorsDataAPI) {
                classNameAnalytics.isShouldModify = true
            }
        } else if (!classNameAnalytics.isAndroidGenerated()) {
            if (extension.useInclude) {
                for (pkgName in include) {
                    if (className.startsWith(pkgName)) {
                        classNameAnalytics.isShouldModify = true
                        break
                    }
                }
            } else {
                classNameAnalytics.isShouldModify = true
                if (!classNameAnalytics.isLeanback()) {
                    for (pkgName in exclude) {
                        if (className.startsWith(pkgName)) {
                            classNameAnalytics.isShouldModify = false
                            break
                        }
                    }
                }
            }
        }
        return classNameAnalytics
    }
}

