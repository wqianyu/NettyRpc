package com.rpc.netty.wuqy;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import string.utils.BlankUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherTest
{
    public static void main(String[] args) {
//        String data = "34dfgdsg[media=x,500,375]%5Bmedia=x,500,375%5D[url]https://vdept.bdstatic.com/6b4433547a7153707a56366a51333434/6c3354634a644838/e6fae44d3895274e7a29281c9f4a22500713d91c8a2ffed42655a3daf05e8eae3ef031da2322200157f2a65846925ef1.mp4?auth_key=1577099372-0-0-65dbeb1c5302006f9ac90058ad69f369[/url][/media][/media][flash]http://img.redocn.com/shejigao/20170103/Redcon_201612291157236356822335.swf[/flash]";
//        Pattern p=Pattern.compile("\\d+");
        String data = "一大串文本，test[attachimg]246325[/attachimg]这个是视频播放地址[media=x,500,375]111111111https://vdept.bdstatic.com/50654c796b35584658766d795a465761/3155454570513772/f7021bf65164683e8de252ad03dc6157ff2c531d01dc9db19228f6a323f4721060cf92cb9a06a869d0c8ed9193e1f1a8.mp4?auth_key=1577178281-0-0-71d02f026f955ea1c0ed5c44d7aa2b40[/media] 这个是flash[flash]http://img.redocn.com/shejigao/20170103/Redcon_201612291157236356822335.swf[/flash]这个是音频音乐[media=x,500,375]https://vdept.bdstatic.com/50654c796b35584658766d795a465761/3155454570513772/f7021bf65164683e8de252ad03dc6157ff2c531d01dc9db19228f6a323f4721060cf92cb9a06a869d0c8ed9193e1f1a8.mp4?auth_key=1577178281-0-0-71d02f026f955ea1c0ed5c44d7aa2b40[/media][audio]http://wma.9ku.com/file2/355/354623.mp3[/audio]附件网络图片[img=1188,1080]http://zonemin.bs2.yy.com/group16/M00/a2b960173504457185bc6d4582647170.jpg[/img][flash]http://img.redocn.com/shejigao/20170103/Redcon_7777777.swf[/flash]";
        /*Pattern p=Pattern.compile("(\\[flash\\])(.*?)(\\[\\/flash\\])");
        Matcher m=p.matcher(data);
        while(m.find()) {
            System.out.println("flash:"+m.group());
        }
        Pattern p2=Pattern.compile("(\\[media=(.*?)\\])(.*?)(\\[/media\\])");
        m=p2.matcher(data);
        while(m.find()) {
            System.out.println("media:"+m.group());
        }*/
        Matcher flash = Pattern.compile(
                Pattern.quote("[flash]")
                        + "(.*?)"
                        + Pattern.quote("[/flash]")
        ).matcher(data);
        while(flash.find()) {
            System.out.println("flash result:"+flash.group(1));
        }

        Matcher media = Pattern.compile("\\[media=(.*?)\\](.*?)(\\[/media\\])"
                ).matcher(data);
        while(media.find()) {
            System.out.println("media result:"+media.group(2));
        }
        Matcher img = Pattern.compile("\\[img=(.*?)\\](.*?)(\\[/img\\])"
        ).matcher(data);
        while(img.find()) {
            System.out.println("img result:"+img.group(2));
        }
        Matcher attachimg = Pattern.compile("\\[attachimg\\](.*?)(\\[/attachimg\\])"
        ).matcher(data);
        while(attachimg.find()) {
            System.out.println("attachimg result:"+attachimg.group(1));
        }

        String data2 = "心有猛虎，细嗅蔷薇<img id=\"aimg_bgdla\" onclick=\"zoom(this, this.src, 0, 0, 0)\" class=\"zoom\" src=\"http://emyfs.bs2cdn.yy.com/ZmNlNDBlOTYtZDZjNC00OGQ0LTk2ZjgtNmE5NjdjMDJmZDI2.jpg\" onmouseover=\"img_onmouseoverfunc(this)\" onload=\"thumbImg(this)\" border=\"0\" alt=\"\" />test";
        Matcher text = Pattern.compile("(<img)(.*?)(/>)"
        ).matcher(data2);
        StringBuilder sb = new StringBuilder();
        String imgSrc = data2;
        while(text.find()) {
            imgSrc = text.group();
            System.out.println("text result:"+text.group(0));
            System.out.println("text result:"+text.start());
            System.out.println("text result:"+text.end());
            if(0 == text.start()) {
                if(data2.length() != text.end()) {
                    sb.append(data2.substring(text.end(), data2.length()));
                } else {
                    //只有签名图片
                }
            } else {
                sb.append(data2.substring(0, text.start()));
                if(data2.length() != text.end()) {
                    sb.append(data2.substring(text.end(), data2.length()));
                }
            }
//            System.out.println("text result:"+text.group(0));
//            System.out.println("text result:"+text.group(1));
//            System.out.println("text result:"+text.group(2));
//            System.out.println("text result:"+text.group(3));
        }
        if(!BlankUtil.isBlank(data2)) {
            System.out.println("imgSrc:"+imgSrc);
            //只有图片
            Matcher src = Pattern.compile("(src=\")(.*?)(\")"
            ).matcher(data2);
            while(src.find()) {
                System.out.println("签名图片 result:"+src.group(2));
            }
        }
        System.out.println("签名文本:"+sb.toString());
        System.out.println("==============================");
        System.out.println(getImg("心有猛虎，细嗅蔷薇<img id=\"aimg_vUeVd\" onclick=\"zoom(this, this.src, 0, 0, 0)\" class=\"zoom\" src=\"http://emyfs.bs2cdn.yy.com/ZmNlNDBlOTYtZDZjNC00OGQ0LTk2ZjgtNmE5NjdjMDJmZDI2.jpg\" onmouseover=\"img_onmouseoverfunc(this)\" onload=\"thumbImg(this)\" border=\"0\" alt=\"\" /><img id=\"aimg_PnGe5\" onclick=\"zoom(this, this.src, 0, 0, 0)\" class=\"zoom\" src=\"http://emyfs.bs2cdn.yy.com/ZmNlNDBlOTYtZDZjNC00OGQ0LTk2ZjgtNmE5NjdjMDJmZDI2.jpg\" onmouseover=\"img_onmouseoverfunc(this)\" onload=\"thumbImg(this)\" border=\"0\" alt=\"\" />"));

    }

    public static List<String> getImg(String data) {
        List<String> list = Lists.newArrayList();
        //只有图片
        Matcher src = Pattern.compile("(src=\")(.*?)(\")"
        ).matcher(data);
        while(src.find()) {
            System.out.println("签名图片 result:"+src.group(2));
            list.add(src.group(2));
        }
        return list;
    }
}
