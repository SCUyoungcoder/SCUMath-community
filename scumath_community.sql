-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: scumath_community
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attention`
--

DROP TABLE IF EXISTS `attention`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `attention` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `focus_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attention`
--

LOCK TABLES `attention` WRITE;
/*!40000 ALTER TABLE `attention` DISABLE KEYS */;
INSERT INTO `attention` VALUES (4,111,113),(7,113,120),(8,113,111),(9,126,113),(10,127,113),(11,127,111);
/*!40000 ALTER TABLE `attention` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog`
--

DROP TABLE IF EXISTS `blog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `blog` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `bid` varchar(200) NOT NULL COMMENT '博客id',
  `title` varchar(200) NOT NULL COMMENT '博客标题',
  `content` longtext NOT NULL COMMENT '博客内容',
  `sort` int(10) NOT NULL DEFAULT '0' COMMENT '博客点赞数量',
  `status` int(11) NOT NULL COMMENT '1:未审批\n0：审批通过',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `author_id` int(11) NOT NULL COMMENT '作者id',
  `author_name` varchar(200) NOT NULL COMMENT '作者名',
  `views` int(10) NOT NULL DEFAULT '0' COMMENT '浏览量',
  `author_avatar` varchar(500) NOT NULL COMMENT '指示是否置顶',
  `category_id` int(10) NOT NULL COMMENT '问题分类id',
  `category_name` varchar(50) NOT NULL COMMENT '问题分类名称',
  `gmt_update` datetime NOT NULL COMMENT '置顶时间，用于显示置顶时排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog`
--

LOCK TABLES `blog` WRITE;
/*!40000 ALTER TABLE `blog` DISABLE KEYS */;
INSERT INTO `blog` VALUES (37,'b32120c6f5854c1a9763b0803818c2c1','12',' 121',0,0,'2021-04-12 20:31:45',111,'asleep',1,'取消置顶',1,'数理逻辑与数学基础','2021-04-12 20:31:45'),(41,'f85e4ba3e8184244a4744841366b4b14','321',' ewqe',0,0,'2021-04-12 22:53:11',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-12 22:53:11'),(42,'c7ce897b666347329b4657436a180741','test',' dsadsadsa',0,0,'2021-04-12 22:53:38',111,'asleep',1,'取消置顶',1,'数理逻辑与数学基础','2021-04-12 22:53:38'),(43,'3374de22e939473bb618db83ad7a9183','删除图片测试',' dsa!',0,0,'2021-04-13 17:42:49',111,'asleep',1,'取消置顶',1,'数理逻辑与数学基础','2021-04-13 17:42:49'),(44,'db6b20ae064445c3a726dee3ada63084','1',' 1',0,0,'2021-04-13 23:07:59',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-13 23:07:59'),(45,'9e673066b7df41f39f21ee5223dc3766','2',' 2',0,0,'2021-04-13 23:11:10',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-13 23:11:10'),(46,'f207c078437f43aab19ac28215100073','3','1',0,0,'2021-04-13 23:11:19',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-13 23:11:19'),(47,'c895dddc7c114b4f9ec1138519aadec0','4',' 4',0,0,'2021-04-13 23:11:26',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-13 23:11:26'),(48,'c48580666cf6421990b99113c02d2ef6','5',' 5',0,0,'2021-04-13 23:11:33',111,'asleep',1,'取消置顶',1,'数理逻辑与数学基础','2021-04-13 23:11:33'),(50,'65ca2ffacf644c218636e08afb3d064d','pic1','1',0,0,'2021-04-14 16:05:09',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-14 16:05:09'),(54,'7e87a7070ed24f0580d190c2149ccbfb','12','1',0,0,'2021-04-14 16:17:02',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-14 16:17:02'),(55,'0f9c39c14c4d472f82966ba4039feb37','1','2',0,0,'2021-04-14 16:58:16',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-14 16:58:16'),(56,'0db237d437c44faaae559d22de528e00','1','1',0,0,'2021-04-14 17:05:43',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-14 17:05:43'),(57,'a22448ae8c2b442fb4bb1248fdae6870','1','2',0,0,'2021-04-14 17:09:29',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-14 17:09:29'),(59,'5cafa6c8e3cb4ee5ada9f3d7ad3bf2c0','1','2',0,0,'2021-04-14 17:14:45',111,'asleep',1,'取消置顶',1,'数理逻辑与数学基础','2021-04-14 17:14:45'),(71,'5ea637a3407a4c87986427be5a170df0','1','![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1618990747302_ab359634fb52435883129231275cb60f.png) ',0,0,'2021-04-21 15:39:10',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-21 15:39:10'),(72,'4b15c99e5bd9484db7dcc1d702829dce','博客test','![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1619090761862_aae8dc5c9f174d5d8b097ec8538d1a6e.png)\r\n\r\n```java\r\npublic String paperList(Model model,\r\n                            @RequestParam(defaultValue = \"a\") String category,\r\n                            @RequestParam(defaultValue = \"1\") int page,\r\n                            @RequestParam(defaultValue = \"10\") int limit){\r\n        PageHelper.startPage(page,limit);\r\n        PageInfo<Paper> info ;\r\n        if (category.length()==1){\r\n            info = new PageInfo<>(paperOfClassService.selectPaperOnlyByStatus(0));\r\n        }\r\n        else {\r\n            List<Paper> papers = elasticsearchClassService.searchPaperByClassAndStatus(category,0);\r\n            info = new PageInfo<>(papers);\r\n            //System.out.println(papers.size());\r\n            int numOfPage = papers.size()/limit;\r\n            List<Paper> showPapers = new ArrayList<>();\r\n            if (papers.size()%limit!=0){\r\n                numOfPage++;\r\n                if (papers.size()/10+1>page){\r\n                    showPapers = papers.subList((page-1)*limit,(page)*limit);\r\n                }\r\n                else {\r\n                    showPapers = papers.subList((page-1)*limit,papers.size());\r\n                }\r\n            }\r\n            else {\r\n                if (papers.size()==0){\r\n\r\n                    showPapers = null;\r\n                }\r\n                else {\r\n                    showPapers = papers.subList((page-1)*limit,(page)*limit);\r\n                }\r\n            }\r\n            if (numOfPage ==0){\r\n                paperOfClassService.selectPaperOnlyByStatus(0);\r\n                //PageHelper,第一个查询自动添加limit和page\r\n                numOfPage++;\r\n            }\r\n``` ',0,0,'2021-04-22 19:26:21',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-22 19:26:21'),(73,'234da8c38dd84495b5457cbbbf59ad2e','数学公式test','$$ X = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}$$\r\n\r\n',0,0,'2021-04-23 17:24:00',111,'asleep',8,'取消置顶',1,'数理逻辑与数学基础','2021-04-23 17:24:00'),(74,'68127add17804ea3ab6edf8a8fbb169b','1','![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1619426673771_5d75bf7a71ee45968771c1fa350c2798.png) ',0,0,'2021-04-26 16:44:37',111,'asleep',0,'取消置顶',1,'数理逻辑与数学基础','2021-04-26 16:44:37'),(75,'edbcfc1c74ad4fa1afa4d10922023b8f','estest','![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1619446843365_ff66434af88842cf9de67bd4174167a7.png) \r\n$$ X = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}$$',0,0,'2021-04-26 22:21:03',111,'asleep',1,'取消置顶',1,'数理逻辑与数学基础','2021-04-26 22:21:03'),(76,'ae39119d3db14b5b8ee7565eeee93c8d','1',' ![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1619524783115_6a4afd0acbaa436980c9ff6f20c6bc2d.png)',1,0,'2021-04-27 19:59:46',111,'asleep',2,'取消置顶',1,'数理逻辑与数学基础','2021-04-27 19:59:46'),(77,'42d3bd2ee6a04d70828f53072398587d','美国页岩气产量分析',' ![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1619533089137_8b354235aefa4674832cb417992101ce.png)\r\n 全美石油生产分析',2,0,'2021-04-27 22:18:37',111,'asleep',28,'取消置顶',1,'数理逻辑与数学基础','2021-05-09 15:49:07'),(78,'ccabfa51f3ab4ed09dd210d270246822','1',' 1',0,0,'2021-04-28 16:40:44',111,'asleep',0,'取消置顶',2,'计算数学','2021-04-28 16:40:44'),(79,'231e74ff37d54177adaf29358c3b526d','博客测试',' ![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1619663632573_97b146b33aa8425fa16fc43834573591.png)\r\n $$ X = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}$$\r\n :relaxed: :relaxed: :smirk: :kissing_closed_eyes: :flushed:',3,0,'2021-04-29 10:34:24',113,'asleep1',20,'取消置顶',1,'数理逻辑与数学基础','2021-04-29 10:34:24'),(80,'e9d1b9279b114be097c0218a85a408ff','1',' 1',0,1,'2021-04-29 10:43:36',120,'asleep2',1,'取消置顶',1,'数理逻辑与数学基础','2021-04-29 10:43:36'),(81,'9e7e67399b5f4c01a1ada39b2bb2aaf3','【CSS3】8款好看的纯CSS3搜索框','\r\n演示效果：\r\n![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1619704269615_ede049a5866a4848a01402d17512ed22.gif)\r\n全部代码如下：\r\n```html\r\n<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n    <title>8款纯CSS3搜索框</title>\r\n\r\n    <link href=\"http://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css\" rel=\"stylesheet\">\r\n    <link rel=\"stylesheet\" href=\"style.css\">\r\n    <style>\r\n        * {\r\n            box-sizing: border-box;\r\n        }\r\n\r\n        body {\r\n            margin: 0;\r\n            padding: 0;\r\n            background: #494A5F;\r\n            font-weight: 500;\r\n            font-family: \"Microsoft YaHei\",\"宋体\",\"Segoe UI\", \"Lucida Grande\", Helvetica, Arial,sans-serif, FreeSans, Arimo;\r\n        }\r\n\r\n        #container {\r\n            width: 500px;\r\n            height: 820px;\r\n            margin: 0 auto;\r\n        }\r\n        div.search {padding: 30px 0;}\r\n\r\n        form {\r\n            position: relative;\r\n            width: 300px;\r\n            margin: 0 auto;\r\n        }\r\n\r\n        input, button {\r\n            border: none;\r\n            outline: none;\r\n        }\r\n\r\n        input {\r\n            width: 100%;\r\n            height: 42px;\r\n            padding-left: 13px;\r\n        }\r\n\r\n        button {\r\n            height: 42px;\r\n            width: 42px;\r\n            cursor: pointer;\r\n            position: absolute;\r\n        }\r\n\r\n        /*搜索框1*/\r\n        .bar1 {background: #A3D0C3;}\r\n        .bar1 input {\r\n            border: 2px solid #7BA7AB;\r\n            border-radius: 5px;\r\n            background: #F9F0DA;\r\n            color: #9E9C9C;\r\n        }\r\n        .bar1 button {\r\n            top: 0;\r\n            right: 0;\r\n            background: #7BA7AB;\r\n            border-radius: 0 5px 5px 0;\r\n        }\r\n        .bar1 button:before {\r\n            content: \"\\f002\";\r\n            font-family: FontAwesome;\r\n            font-size: 16px;\r\n            color: #F9F0DA;\r\n        }\r\n\r\n        /*搜索框2*/\r\n        .bar2 {background: #DABB52;}\r\n        .bar2 input, .bar2 button {\r\n            border-radius: 3px;\r\n        }\r\n        .bar2 input {\r\n            background: #F9F0DA;\r\n        }\r\n        .bar2 button {\r\n            height: 26px;\r\n            width: 26px;\r\n            top: 8px;\r\n            right: 8px;\r\n            background: #F15B42;\r\n        }\r\n        .bar2 button:before {\r\n            content: \"\\f105\";\r\n            font-family: FontAwesome;\r\n            color: #F9F0DA;\r\n            font-size: 20px;\r\n            font-weight: bold;\r\n        }\r\n\r\n        /*搜索框3*/\r\n        .bar3 {background: #F9F0DA;}\r\n        .bar3 form {background: #A3D0C3;}\r\n        .bar3 input, .bar3 button {\r\n            background: transparent;\r\n        }\r\n        .bar3 button {\r\n            top: 0;\r\n            right: 0;\r\n        }\r\n        .bar3 button:before {\r\n            content: \"\\f002\";\r\n            font-family: FontAwesome;\r\n            font-size: 16px;\r\n            color: #F9F0DA;\r\n        }\r\n\r\n        /*搜索框4*/\r\n        .bar4 {background: #F15B42;}\r\n        .bar4 form {\r\n            background: #F9F0DA;\r\n            border-bottom: 2px solid #BE290E;\r\n        }\r\n        .bar4 input, .bar4 button {\r\n            background: transparent;\r\n        }\r\n        .bar4 button {\r\n            top: 0;\r\n            right: 0;\r\n        }\r\n        .bar4 button:before {\r\n            content: \"\\f178\";\r\n            font-family: FontAwesome;\r\n            font-size: 20px;\r\n            color: #be290e;\r\n        }\r\n\r\n        /*搜索框5*/\r\n        .bar5 {background: #683B4D;}\r\n        .bar5 input, .bar5 button {\r\n            background: transparent;\r\n        }\r\n        .bar5 input {\r\n            border: 2px solid #F9F0DA;\r\n        }\r\n        .bar5 button {\r\n            top: 0;\r\n            right: 0;\r\n        }\r\n        .bar5 button:before {\r\n            content: \"\\f002\";\r\n            font-family: FontAwesome;\r\n            font-size: 16px;\r\n            color: #F9F0DA;\r\n        }\r\n        .bar5 input:focus {\r\n            border-color: #311c24\r\n        }\r\n\r\n        /*搜索框6*/\r\n        .bar6 {background: #F9F0DA;}\r\n        .bar6 input {\r\n            border: 2px solid #c5464a;\r\n            border-radius: 5px;\r\n            background: transparent;\r\n            top: 0;\r\n            right: 0;\r\n        }\r\n        .bar6 button {\r\n            background: #c5464a;\r\n            border-radius: 0 5px 5px 0;\r\n            width: 60px;\r\n            top: 0;\r\n            right: 0;\r\n        }\r\n        .bar6 button:before {\r\n            content: \"搜索\";\r\n            font-size: 13px;\r\n            color: #F9F0DA;\r\n        }\r\n\r\n\r\n        /*搜索框7*/\r\n        .bar7 {background: #7BA7AB;}\r\n        .bar7 form {\r\n            height: 42px;\r\n        }\r\n        .bar7 input {\r\n            width: 250px;\r\n            border-radius: 42px;\r\n            border: 2px solid #324B4E;\r\n            background: #F9F0DA;\r\n            transition: .3s linear;\r\n            float: right;\r\n        }\r\n        .bar7 input:focus {\r\n            width: 300px;\r\n        }\r\n        .bar7 button {\r\n            background: none;\r\n            top: -2px;\r\n            right: 0;\r\n        }\r\n        .bar7 button:before{\r\n          content: \"\\f002\";\r\n          font-family: FontAwesome;\r\n          color: #324b4e;\r\n        }\r\n\r\n        /*搜索框8*/\r\n        .bar8 {background: #B46381;}\r\n        .bar8 form {\r\n            height: 42px;\r\n        }\r\n        .bar8 input {\r\n            width: 0;\r\n            padding: 0 42px 0 15px;\r\n            border-bottom: 2px solid transparent;\r\n            background: transparent;\r\n            transition: .3s linear;\r\n            position: absolute;\r\n            top: 0;\r\n            right: 0;\r\n            z-index: 2;\r\n        }\r\n        .bar8 input:focus {\r\n            width: 300px;\r\n            z-index: 1;\r\n            border-bottom: 2px solid #F9F0DA;\r\n        }\r\n        .bar8 button {\r\n            background: #683B4D;\r\n            top: 0;\r\n            right: 0;\r\n        }\r\n        .bar8 button:before {\r\n            content: \"\\f002\";\r\n            font-family: FontAwesome;\r\n            font-size: 16px;\r\n            color: #F9F0DA;\r\n        }\r\n    </style>\r\n</head>\r\n<body>\r\n<div id=\"container\">\r\n    <div class=\"search bar1\">\r\n        <form>\r\n            <input type=\"text\" placeholder=\"请输入您要搜索的内容...\">\r\n            <button type=\"submit\"></button>\r\n        </form>\r\n    </div>\r\n\r\n    <div class=\"search bar2\">\r\n        <form>\r\n            <input type=\"text\" placeholder=\"请输入您要搜索的内容...\">\r\n            <button type=\"submit\"></button>\r\n        </form>\r\n    </div>\r\n\r\n    <div class=\"search bar3\">\r\n        <form>\r\n            <input type=\"text\" placeholder=\"请输入您要搜索的内容...\">\r\n            <button type=\"submit\"></button>\r\n        </form>\r\n    </div>\r\n\r\n    <div class=\"search bar4\">\r\n        <form>\r\n            <input type=\"text\" placeholder=\"请输入您要搜索的内容...\">\r\n            <button type=\"submit\"></button>\r\n        </form>\r\n    </div>\r\n\r\n    <div class=\"search bar5\">\r\n        <form>\r\n            <input type=\"text\" placeholder=\"请输入您要搜索的内容...\">\r\n            <button type=\"submit\"></button>\r\n        </form>\r\n    </div>\r\n\r\n    <div class=\"search bar6\">\r\n        <form>\r\n            <input type=\"text\" placeholder=\"请输入您要搜索的内容...\">\r\n            <button type=\"submit\"></button>\r\n        </form>\r\n    </div>\r\n\r\n    <div class=\"search bar7\">\r\n        <form>\r\n            <input type=\"text\" placeholder=\"请输入您要搜索的内容...\">\r\n            <button type=\"submit\"></button>\r\n        </form>\r\n    </div>\r\n\r\n    <div class=\"search bar8\">\r\n        <form>\r\n            <input type=\"text\" placeholder=\"请输入您要搜索的内容...\">\r\n            <button type=\"submit\"></button>\r\n        </form>\r\n    </div>\r\n</div>\r\n</body>\r\n</html>\r\n```\r\n',0,0,'2021-04-29 21:54:03',111,'asleep',12,'取消置顶',1,'数理逻辑与数学基础','2021-04-29 21:54:03'),(82,'64c9a4c4134e426b9733872305933265','edit测试非管理员测试2',' ![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1619705071036_f11a3526e7504106b9ca830ded41698f.png)',0,1,'2021-04-29 22:04:39',120,'asleep2',7,'取消置顶',2,'计算数学','2021-04-29 22:04:39'),(83,'9c6d78db688e41289a23b9440250384f','非管理员测试3','非管理员测试3',2,0,'2021-04-29 22:04:58',120,'asleep2',45,'置顶',1,'数理逻辑与数学基础','2021-05-10 10:44:32'),(84,'d9be20c307b5430d83b7db45ad91ab08','申请测试1',' 1',0,0,'2021-05-07 14:59:21',113,'asleep1',3,'取消置顶',1,'数理逻辑与数学基础','2021-05-07 14:59:21'),(85,'8f4d80da30dc426f8805732b94b787de','申请测试2',' 2',0,0,'2021-05-07 14:59:36',113,'asleep1',8,'取消置顶',1,'数理逻辑与数学基础','2021-05-07 14:59:36'),(87,'3621af80417c4526afe8ea418fdeba56','申请测试3',' ',0,0,'2021-05-07 15:43:29',113,'asleep1',0,'取消置顶',1,'数理逻辑与数学基础','2021-05-07 15:43:29'),(88,'bc2a4fc0a8364b68af343f27c9fa88ae','1','![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1620549832991_12cb34c849f04a55bfedd7ae2f23231f.png) \r\ndsadsa:confused: :hushed::triumph:',0,1,'2021-05-09 16:44:59',126,'asleep9',0,'取消置顶',2,'计算数学','2021-05-09 16:44:59'),(89,'163d7377e9a048e4bad22328d6f55325','博客测试','```html\r\n<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n    <title>8款纯CSS3搜索框</title>\r\n    <link href=\"http://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css\" rel=\"stylesheet\">\r\n    <link rel=\"stylesheet\" href=\"style.css\">\r\n    <style>\r\n        * {\r\n            box-sizing: border-box;\r\n        }\r\n        body {\r\n            margin: 0;\r\n            padding: 0;\r\n            background: #494A5F;\r\n            font-weight: 500;\r\n            font-family: \"Microsoft YaHei\",\"宋体\",\"Segoe UI\", \"Lucida Grande\", Helvetica, Arial,sans-serif, FreeSans, Arimo;\r\n        }\r\n        #container {\r\n            width: 500px;\r\n            height: 820px;\r\n            margin: 0 auto;\r\n        }\r\n        div.search {padding: 30px 0;}\r\n        form {\r\n            position: relative;\r\n            width: 300px;\r\n            margin: 0 auto;\r\n        }\r\n        input, button {\r\n            border: none;\r\n            outline: none;\r\n        }\r\n        input {\r\n            width: 100%;\r\n            height: 42px;\r\n            padding-left: 13px;\r\n        }\r\n        button {\r\n            height: 42px;\r\n            width: 42px;\r\n            cursor: pointer;\r\n            position: absolute;\r\n        }\r\n        /*搜索框1*/\r\n        .bar1 {background: #A3D0C3;}\r\n        .bar1 input {\r\n            border: 2px solid #7BA7AB;\r\n            border-radius: 5px;\r\n            background: #F9F0DA;\r\n            color: #9E9C9C;\r\n        }\r\n        .bar1 button {\r\n            top: 0;\r\n            right: 0;\r\n            background: #7BA7AB;\r\n            border-radius: 0 5px 5px 0;\r\n        }\r\n        .bar1 button:before {\r\n            content: \"\\f002\";\r\n            font-family: FontAwesome;\r\n            font-size: 16px;\r\n            color: #F9F0DA;\r\n        }\r\n        /*搜索框2*/\r\n        .bar2 {background: #DABB52;}\r\n        .bar2 input, .bar2 button {\r\n            border-radius: 3px;\r\n        }\r\n``` ',0,1,'2021-05-09 18:41:56',127,'测试用户',2,'取消置顶',2,'计算数学','2021-05-09 18:41:56'),(90,'f0bdcd035ca443afbd7dc6324e8cbf8c','1','dsa![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1620557990950_8f9e5ac80b924942a070bc9e6c104646.png)',1,0,'2021-05-09 18:59:27',113,'asleep1',0,'取消置顶',1,'数理逻辑与数学基础','2021-05-09 18:59:27'),(91,'b6ac6b7decac413ca94f6031ebe901e4','1','![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1620872246403_be5adc1aed8b4535a88c7b6c8da57abc.jpg) ',0,0,'2021-05-13 10:17:32',113,'asleep1',0,'取消置顶',1,'数理逻辑与数学基础','2021-05-13 10:17:32');
/*!40000 ALTER TABLE `blog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_able`
--

DROP TABLE IF EXISTS `blog_able`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `blog_able` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT '指示博客作者id',
  `type` int(11) NOT NULL COMMENT '0:申请1:小组2:user（如果数据库查blog_id不到数据，就当所有人可见）',
  `entity_id` int(11) NOT NULL COMMENT '对应type的id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_able`
--

LOCK TABLES `blog_able` WRITE;
/*!40000 ALTER TABLE `blog_able` DISABLE KEYS */;
INSERT INTO `blog_able` VALUES (17,87,113,1,13),(25,87,113,2,126),(26,89,127,2,113),(27,89,127,2,111),(28,90,113,2,120),(29,90,113,1,14);
/*!40000 ALTER TABLE `blog_able` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classification`
--

DROP TABLE IF EXISTS `classification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `classification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `searchname` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classification`
--

LOCK TABLES `classification` WRITE;
/*!40000 ALTER TABLE `classification` DISABLE KEYS */;
INSERT INTO `classification` VALUES (1,'数理逻辑与数学基础','logic'),(2,'计算数学','compute'),(3,'数论','number'),(4,'代数','algebra'),(5,'几何','geometry'),(6,'拓扑','topology'),(7,'数学分析','analysis'),(8,'常微分方程','ODE'),(9,'偏微分方程','PDE'),(10,'动力系统','dynamical'),(11,'泛函分析','functional'),(12,'概率论','probability'),(13,'统计学','statistics'),(14,'运筹学','opsearch'),(15,'组合数学','combinatorial'),(16,'模糊数学','fuzzy'),(17,'量子数学','quantum'),(18,'应用数学','applied');
/*!40000 ALTER TABLE `classification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '评论者id',
  `entity_type` int(11) NOT NULL COMMENT '评论对象的类型;0:paper;1:用户，2blog，3question，4Source',
  `entity_id` int(11) NOT NULL COMMENT '评论对象的id',
  `target_id` int(11) DEFAULT NULL COMMENT '针对于回复用户评论的情况，用于指定回复用户的id',
  `target_id2` int(11) NOT NULL COMMENT '提示一级和二级评论的target',
  `content` text NOT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0:评论，1：公告/系统通知',
  `create_time` timestamp NOT NULL,
  `type` int(11) DEFAULT NULL COMMENT '0普通评论，1解决问答的评论（置顶）',
  `notice_table` int(11) DEFAULT NULL COMMENT '公告的用户群标签；111：全体用户，0：普通用户，1：超级管理员\\\\n改为0普通评论，1解决问答的评论（置顶）',
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`) /*!80000 INVISIBLE */,
  KEY `index_entity_id` (`entity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (6,111,0,0,0,0,'公告测试1',1,'2020-12-24 06:24:43',0,0),(62,115,0,32,115,0,'悬赏问题评论1',0,'2021-02-08 04:37:57',0,0),(63,118,0,35,118,0,'2',0,'2021-02-22 07:11:28',0,0),(64,118,1,63,118,0,'2',0,'2021-02-22 07:11:32',0,0),(65,118,1,63,118,0,'2',0,'2021-02-22 07:11:34',0,0),(66,118,1,63,118,0,'e2',0,'2021-02-22 07:11:41',1,0),(73,111,2,0,0,0,'123',0,'2021-04-13 06:57:24',0,0),(74,111,2,0,0,0,'123',0,'2021-04-13 07:00:18',0,0),(75,111,2,35,0,0,'12',0,'2021-04-13 07:05:03',0,0),(76,111,2,75,0,0,'',0,'2021-04-13 07:18:43',0,0),(77,111,2,75,0,0,'dsa',0,'2021-04-13 07:18:57',0,0),(78,111,2,75,0,0,'123',0,'2021-04-13 07:24:14',0,0),(79,111,2,35,0,0,'123',0,'2021-04-13 07:24:24',0,0),(80,111,2,75,0,0,'的撒大卅656464dsada',0,'2021-04-13 07:24:30',0,0),(81,111,2,75,0,0,'dsa',0,'2021-04-13 07:24:49',0,0),(82,111,0,75,0,0,'的撒大卅656464dsada',0,'2021-04-13 07:34:05',0,0),(83,111,1,75,0,0,'121313214',0,'2021-04-13 07:40:25',0,0),(84,111,1,79,0,0,'wewq',0,'2021-04-13 07:52:07',0,0),(85,111,2,75,111,0,'的撒大卅656464dsada',0,'2021-04-13 08:02:29',0,0),(86,111,2,79,111,0,'123',0,'2021-04-13 08:02:56',0,0),(87,111,1,79,111,0,'ssssssssssddddddd',0,'2021-04-13 08:04:45',0,0),(88,111,1,75,111,0,'的撒大卅656464dsada',0,'2021-04-13 08:04:51',0,0),(89,111,1,79,111,0,'',0,'2021-04-13 08:18:11',0,0),(90,111,3,43,0,0,'dsad',0,'2021-04-13 11:12:16',0,0),(91,111,3,43,0,0,'的撒大',0,'2021-04-13 11:12:34',0,0),(92,111,2,43,0,0,'1',0,'2021-04-13 11:15:24',0,0),(93,111,1,92,0,0,'2',0,'2021-04-13 11:15:29',0,0),(94,111,1,92,0,0,'3',0,'2021-04-13 11:15:35',0,0),(95,111,2,43,0,0,'4',0,'2021-04-13 11:15:43',0,0),(96,111,1,95,0,0,'5',0,'2021-04-13 11:15:48',0,0),(97,111,1,95,0,0,'6',0,'2021-04-13 11:15:58',0,0),(98,111,2,43,0,0,'7\r\n',0,'2021-04-13 15:04:11',0,0),(99,111,2,43,0,0,'8',0,'2021-04-13 15:04:14',0,0),(100,111,2,43,0,0,'9',0,'2021-04-13 15:04:17',0,0),(101,111,2,43,0,0,'10',0,'2021-04-13 15:04:22',0,0),(102,111,2,43,0,0,'11',0,'2021-04-13 15:04:24',0,0),(103,111,2,43,0,0,'12',0,'2021-04-13 15:04:27',0,0),(104,111,2,43,0,0,'13',0,'2021-04-13 15:04:30',0,0),(105,111,2,43,0,0,'14',0,'2021-04-13 15:04:35',0,0),(106,111,2,43,0,0,'15',0,'2021-04-13 15:05:03',0,0),(107,111,2,43,0,0,'16',0,'2021-04-13 15:05:12',0,0),(108,111,1,92,111,0,'18',0,'2021-04-14 01:39:30',0,0),(109,111,1,92,111,0,'19',0,'2021-04-14 01:39:48',0,0),(110,111,3,70,0,0,'q',0,'2021-04-15 09:00:19',0,0),(111,111,1,110,0,0,'e',0,'2021-04-15 09:00:22',0,0),(112,111,1,110,111,0,'ewq',0,'2021-04-15 09:00:27',0,0),(113,111,3,70,0,0,'ewq',0,'2021-04-15 09:00:31',0,1),(114,111,1,113,0,0,'q',0,'2021-04-15 09:00:33',0,0),(115,111,1,113,111,0,'ewq',0,'2021-04-15 09:00:39',0,0),(122,111,0,35,118,0,'1',0,'2021-04-21 07:24:36',0,0),(123,111,1,122,111,0,'2',0,'2021-04-21 07:24:41',0,0),(124,111,1,122,111,0,'3',0,'2021-04-21 07:24:47',1,0),(126,111,3,76,0,0,'一级评论',0,'2021-04-23 11:05:08',0,1),(127,111,1,126,0,0,'二级评论',0,'2021-04-23 11:05:18',0,0),(128,111,1,126,111,0,'三级评论',0,'2021-04-23 11:05:30',0,0),(129,111,3,76,0,0,'一级评论2',0,'2021-04-23 11:05:41',0,0),(130,111,1,129,0,0,'二级评论2',0,'2021-04-23 11:05:48',0,0),(131,111,1,129,111,0,'三级评论2',0,'2021-04-23 11:07:32',0,0),(132,111,2,73,0,0,'一级评论1',0,'2021-04-23 11:18:58',0,0),(133,111,2,73,0,0,'一级评论2',0,'2021-04-23 11:19:05',0,0),(134,111,1,132,0,0,'二级评论1',0,'2021-04-23 11:19:14',0,0),(135,111,1,132,111,0,'三级评论1',0,'2021-04-23 11:19:27',0,0),(136,111,1,133,0,0,'二级评论2',0,'2021-04-23 11:19:36',0,0),(137,111,1,133,111,0,'三级评论2',0,'2021-04-23 11:19:48',0,0),(138,111,1,133,111,0,'',0,'2021-04-23 11:19:53',0,0),(142,111,2,59,0,111,'1',0,'2021-04-24 14:25:31',0,0),(143,111,2,73,0,111,'1',0,'2021-04-24 14:38:30',0,0),(144,111,2,73,0,111,'2',0,'2021-04-24 14:38:32',0,0),(145,111,2,73,0,111,'3',0,'2021-04-24 14:38:35',0,0),(146,111,2,73,0,111,'4',0,'2021-04-24 14:38:37',0,0),(147,111,2,73,0,111,'5',0,'2021-04-24 14:38:39',0,0),(148,111,2,73,0,111,'6',0,'2021-04-24 14:38:43',0,0),(149,111,2,73,0,111,'7',0,'2021-04-24 14:38:45',0,0),(150,111,2,73,0,111,'8',0,'2021-04-24 14:38:48',0,0),(151,111,2,73,0,111,'9',0,'2021-04-24 14:38:51',0,0),(152,111,3,76,0,111,'3',0,'2021-04-24 15:02:57',0,0),(153,111,3,76,0,111,'4',0,'2021-04-24 15:02:59',0,0),(154,111,3,76,0,111,'5',0,'2021-04-24 15:03:02',0,0),(155,111,3,76,0,111,'6',0,'2021-04-24 15:03:04',0,0),(156,111,3,76,0,111,'7',0,'2021-04-24 15:03:05',0,0),(157,111,3,76,0,111,'8',0,'2021-04-24 15:03:07',0,0),(158,111,3,76,0,111,'9',0,'2021-04-24 15:03:09',0,0),(159,111,3,76,0,111,'10',0,'2021-04-24 15:03:12',0,0),(160,111,3,76,0,111,'11',0,'2021-04-24 15:03:14',0,0),(182,111,3,71,0,111,'12',0,'2021-04-26 06:26:49',0,1),(183,111,3,75,0,111,'1',0,'2021-04-26 07:37:32',0,1),(184,111,3,75,0,111,'2',0,'2021-04-26 07:37:33',0,0),(185,113,2,79,0,113,'1',0,'2021-04-29 02:36:29',0,0),(186,113,1,185,0,113,'2',0,'2021-04-29 02:36:33',0,0),(187,113,1,185,113,113,'3',0,'2021-04-29 02:36:37',0,0),(188,113,3,80,0,113,'1',0,'2021-04-29 02:37:38',0,0),(189,113,3,80,0,113,'2',0,'2021-04-29 02:37:39',0,1),(190,113,3,80,0,113,'3',0,'2021-04-29 02:37:41',0,0),(191,113,0,60,111,111,'1',0,'2021-04-29 02:37:50',0,0),(192,113,1,191,0,113,'2',0,'2021-04-29 02:37:53',0,0),(193,113,1,191,113,113,'3',0,'2021-04-29 02:37:57',1,0),(194,111,3,79,0,111,'1',0,'2021-04-29 02:40:23',0,0),(195,111,1,194,0,111,'2',0,'2021-04-29 02:40:26',0,0),(196,111,1,194,111,111,'3',0,'2021-04-29 02:40:35',0,0),(197,111,3,80,0,113,'1',0,'2021-04-29 02:40:47',0,0),(198,111,3,80,0,113,'2',0,'2021-04-29 02:40:49',0,0),(199,111,3,80,0,113,'3',0,'2021-04-29 02:40:51',0,0),(200,120,2,77,0,111,'1',0,'2021-04-29 02:42:28',0,0),(201,120,2,79,0,113,'1',0,'2021-04-29 02:42:40',0,0),(202,120,2,79,0,113,'2',0,'2021-04-29 02:42:43',0,0),(203,120,2,79,0,113,'3',0,'2021-04-29 02:42:47',0,0),(204,120,2,79,0,113,'4',0,'2021-04-29 02:42:52',0,0),(205,120,2,79,0,113,'5',0,'2021-04-29 02:42:55',0,0),(206,120,2,79,0,113,'6',0,'2021-04-29 02:43:01',0,0),(207,120,2,79,0,113,'7',0,'2021-04-29 02:43:04',0,0),(208,120,2,79,0,113,'8',0,'2021-04-29 02:43:07',0,0),(209,120,2,79,0,113,'9',0,'2021-04-29 02:43:15',0,0),(210,120,3,80,0,113,'32',0,'2021-04-29 02:44:20',0,0),(211,111,3,79,0,111,'4',0,'2021-04-29 14:03:29',0,0),(212,111,3,79,0,111,'5',0,'2021-04-29 14:03:31',0,0),(213,120,3,80,0,113,'31',0,'2021-04-29 14:06:17',0,0),(214,120,3,80,0,113,'32',0,'2021-04-29 14:06:19',0,0),(215,113,2,83,0,120,'da',0,'2021-05-06 07:14:45',0,0),(216,126,0,70,113,113,'1',0,'2021-05-09 08:25:43',0,0),(217,126,3,81,0,120,'1',0,'2021-05-09 08:25:48',0,0),(218,126,2,83,0,120,'123',0,'2021-05-09 08:41:57',0,0),(219,126,1,215,0,113,'1',0,'2021-05-09 08:43:12',0,0),(220,126,1,215,126,113,'2',0,'2021-05-09 08:43:20',0,0),(221,126,0,67,111,111,'1',0,'2021-05-09 08:46:51',0,0),(222,127,1,215,0,113,'的撒大卅656464dsada',0,'2021-05-09 10:28:58',0,0),(223,127,1,215,0,113,'1',0,'2021-05-09 10:29:03',0,0),(224,127,1,199,0,111,'1',0,'2021-05-09 10:36:28',0,0),(225,127,4,74,113,113,'1',0,'2021-05-09 10:37:03',0,0),(226,127,1,225,0,127,'1',0,'2021-05-09 10:37:05',0,0),(227,127,1,225,127,127,'1',0,'2021-05-09 10:37:08',1,0),(228,127,2,81,0,111,'非常有用',0,'2021-05-09 10:39:14',0,0),(229,127,1,228,0,127,'学到了',0,'2021-05-09 10:39:26',0,0),(230,111,3,83,0,113,'解决测试1',0,'2021-05-09 10:48:47',0,1),(231,111,3,83,0,113,'解决测试2',0,'2021-05-09 10:48:52',0,0),(232,111,3,83,0,113,'解决测试3',0,'2021-05-09 10:48:57',0,0),(233,113,3,84,0,113,'1',0,'2021-05-10 01:54:11',0,1),(234,113,3,84,0,113,'2',0,'2021-05-10 01:54:13',0,0);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_member`
--

DROP TABLE IF EXISTS `group_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `group_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `username` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_member`
--

LOCK TABLES `group_member` WRITE;
/*!40000 ALTER TABLE `group_member` DISABLE KEYS */;
INSERT INTO `group_member` VALUES (14,4,120,'asleep2'),(16,4,111,'asleep'),(17,14,120,'asleep2'),(18,14,111,'asleep'),(19,15,113,'asleep1'),(20,17,113,'asleep1');
/*!40000 ALTER TABLE `group_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invitation_code`
--

DROP TABLE IF EXISTS `invitation_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `invitation_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `admin_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitation_code`
--

LOCK TABLES `invitation_code` WRITE;
/*!40000 ALTER TABLE `invitation_code` DISABLE KEYS */;
INSERT INTO `invitation_code` VALUES (1,'scumath',111),(3,'scumath123',113);
/*!40000 ALTER TABLE `invitation_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_ticket`
--

DROP TABLE IF EXISTS `login_ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `login_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL COMMENT ' 0-普通用户; 1-超级管理员',
  `ticket` varchar(45) NOT NULL COMMENT '随机字符串，用于保持登录状态',
  `status` int(11) DEFAULT '0' COMMENT '0-有效; 1-无效;',
  `expired` timestamp NOT NULL COMMENT '登录过期时间',
  PRIMARY KEY (`id`),
  KEY `index_ticket` (`ticket`(20))
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login_ticket`
--

LOCK TABLES `login_ticket` WRITE;
/*!40000 ALTER TABLE `login_ticket` DISABLE KEYS */;
INSERT INTO `login_ticket` VALUES (3,1,2,'dsadsadsadsaw',5,'2020-12-24 06:24:39'),(4,111,0,'4d7336f827e74ff1b1ad176be05fe455',1,'2020-12-25 19:51:40'),(5,111,0,'cc355975766b40aba0c5d77dc827f7ad',1,'2020-12-26 03:23:17'),(6,111,0,'09786a1d9e0141379380a69f06f60a35',1,'2020-12-26 03:25:10'),(7,111,0,'3456faba4686474faf6daf7543d1b89b',1,'2020-12-26 18:31:27'),(8,112,0,'78b044fc6e7b4515a7903b9a87bc6fdb',1,'2020-12-26 20:56:08'),(9,112,0,'ca1e1f234d564c5a80735d0fce3e9052',1,'2020-12-26 20:57:17'),(10,112,0,'1f37532f9b3043dd960347a59ed89641',1,'2020-12-26 22:06:08'),(11,111,0,'73bc7c723619414ca1fac36ae936d551',1,'2020-12-29 00:23:17'),(12,111,1,'68a36dd5c5ea4dc29dc5e1f1f52164fd',0,'2020-12-29 11:17:33'),(13,111,1,'0df1142070d44323bb5cc4b0eb62df76',0,'2020-12-30 03:52:08'),(14,111,1,'3b8a5be9244f4558a7d2d5b7f526fe7b',0,'2020-12-31 18:23:25'),(15,111,1,'232f57bfd26649e28435360c36314835',1,'2021-01-01 22:45:26'),(16,111,1,'a1aa10ae1ebd474b8964e71a6db3651b',0,'2021-01-02 01:15:16'),(17,111,1,'32015da9619044c5ae28c45902aff3f0',0,'2021-01-02 03:27:20'),(18,111,1,'e5ad5a58ae0641288147c7fd9e8582b4',1,'2021-01-02 17:28:34'),(19,111,1,'b05ff2b30d6c4e9ca1d088972ad940e9',1,'2021-01-02 17:36:49'),(20,111,1,'8098cc36576d45be8e499b56d5cc7238',1,'2021-01-03 04:34:09'),(21,111,1,'cfd2b5d56d1a4e04ba14ca254fc4848d',0,'2021-01-03 04:38:04'),(22,111,1,'5c1656de4679487980636895e761455d',0,'2021-01-03 18:30:00'),(23,111,1,'9938c5e92ea04aac979bb6dc4ec8003f',1,'2021-01-03 19:42:44'),(24,111,1,'6e4ad5a94dc349eba0065cc8597c397e',0,'2021-01-03 19:54:22'),(25,111,1,'07b66139c7244d68ab8fc28c2fc04881',1,'2021-01-04 18:42:33'),(26,111,1,'06d512673dc14403b659131deeef8ba4',1,'2021-01-04 20:00:16'),(27,113,0,'681418c4477447338d578194c56f8b92',1,'2021-01-04 22:04:23'),(28,111,1,'920fa259329d467ca42ec6fce95af7dc',0,'2021-01-04 22:21:38'),(29,111,1,'b167b097e88e48b5a02a9d8efeefe11d',1,'2021-01-05 01:00:25'),(30,111,1,'72cb061c31534cb0b62d3582d3e14054',1,'2021-01-05 01:00:52'),(31,111,1,'713d7ef5fa42407687153dcdeb8feb14',1,'2021-01-05 00:13:51'),(32,113,0,'6e4340427a614dc9bd54b07a41033fb6',1,'2021-01-05 00:47:58'),(33,111,1,'31631a0861754abfa259a25418afb6c0',1,'2021-01-05 00:49:45'),(34,113,0,'d6220705408143468fd7a0e7f5a5c21c',1,'2021-01-05 00:52:16'),(35,111,1,'7da0a45dea7b4e92949f7f7057833e36',1,'2021-01-05 00:53:00'),(36,113,0,'dba4c38e276a4a91be349ffdfdcc44d9',0,'2021-01-05 00:58:59'),(37,111,1,'474f03b4e01d4d9aad54a36774b11949',0,'2021-01-05 15:18:35'),(38,111,1,'570b8c02119a4085bdfe5152f72d7501',0,'2021-01-17 19:44:17'),(39,114,0,'2b38a807b0114f8fadc18e88ae6f146b',0,'2021-01-27 03:42:33'),(40,114,0,'05e10ae9bb8a42cc8b43d5e6171f541f',0,'2021-01-27 20:32:05'),(41,115,0,'50c26e29237147259af2c5b8b88ef7cb',0,'2021-01-27 23:49:29'),(42,114,0,'b8995297dfa2463aafa79934ba98a712',1,'2021-01-28 13:28:00'),(43,115,0,'cb784a83adee40968c0a64a357eb48a8',0,'2021-01-28 22:10:30'),(44,114,1,'1b38f984133d4082b597aba0080d13fe',0,'2021-01-29 03:01:17'),(45,111,1,'6bad815cf8294924ae903f949c947490',0,'2021-01-31 19:22:10'),(46,111,1,'527142a7ef054127bd3625b2dfc48fff',1,'2021-02-03 21:29:34'),(47,111,1,'31317f2d043e4bc39c9c7d75da23ffb7',0,'2021-02-03 21:37:24'),(48,111,1,'8bb3f9d627f740809276edb17ad0395f',0,'2021-02-03 21:55:13'),(49,115,0,'1df4147e373d40d09e53b5180aa9fcc8',0,'2021-02-04 21:54:49'),(50,115,0,'cee16b4cba1a493abcc551e1be3c51ad',0,'2021-02-05 11:24:06'),(51,114,1,'88fddee37b5a4a15874e94bb181d79f1',0,'2021-02-05 15:04:11'),(52,115,0,'26ffa23eb9484c2faa208ffc614b3d8b',0,'2021-02-06 00:52:20'),(53,115,0,'a33bd972f5b44946948de97d234e999c',0,'2021-02-08 16:37:30'),(54,111,1,'ac5d607dbdc94bffa6adf11c65dbfe4a',1,'2021-02-16 18:56:15'),(55,111,1,'24f3d918ec544784bafefeddc620bcdd',0,'2021-02-16 18:57:46'),(56,118,0,'f6f3b26a1faa429486cd7301999ce7fa',0,'2021-02-21 01:57:12'),(57,118,0,'0511830a39e44b5c97970511973842d5',0,'2021-02-21 12:58:56'),(58,118,1,'21b3edfb3b174e4ea306a5eb0ce98b81',0,'2021-02-22 19:11:15'),(59,111,1,'5c7020b321ef432295cab8f850f80bd4',0,'2021-04-05 20:57:55'),(60,111,1,'25b2d12896674dd3b60c14e0673af727',0,'2021-04-10 17:47:02'),(61,111,1,'800b2f36aaa345c482141592a6953180',0,'2021-04-11 18:07:47'),(62,111,1,'2fdcf0cf793e4912b135d432851a9fcb',1,'2021-04-12 22:22:56'),(63,111,1,'3f51fd28ef794b4b97dd38ab8686b93a',0,'2021-04-13 02:00:31'),(64,111,1,'7ac7001028e3423295a65d31a1640d51',0,'2021-04-13 03:05:31'),(65,111,1,'118312388b2f4640b8acc088ad635c09',0,'2021-04-13 18:57:10'),(66,111,1,'3415cfd6ba484ad8ab07f83927b94f72',1,'2021-04-14 13:39:15'),(67,119,0,'dadfb6d6e87b4a8186325d2f841f1ecb',1,'2021-04-14 14:29:57'),(68,111,1,'187bf997019a42d3a3f46b3a6c306fdc',0,'2021-04-14 14:31:28'),(69,111,1,'e535722036054f8aa8c2fc0e2e2ae5e1',0,'2021-04-14 19:53:43'),(70,111,1,'2095bbf28b3d45aabc7b97914ad6fab6',0,'2021-04-15 20:59:25'),(71,111,1,'5da79f5948994349b405e139b4e8817e',0,'2021-04-16 20:30:44'),(72,111,1,'0f0b7ce6835c458bb5389725b6f8973d',0,'2021-04-17 19:20:25'),(73,111,1,'097af3634522456980cee1661c67792c',0,'2021-04-17 20:37:22'),(74,111,1,'ace58aecbdaf4f6fa2caac88b3d2c16c',0,'2021-04-17 21:21:47'),(75,113,0,'51d77b70b7504705a2dfa5fe24f68b89',0,'2021-04-18 02:47:43'),(76,111,1,'29ff8cd110dd4d2899211b658936ac64',1,'2021-04-19 22:27:44'),(77,111,1,'3e314c342da246cb8e0538023754ab92',0,'2021-04-20 02:11:41'),(78,111,1,'0af13f1c293a4339b8396e253e94c1b0',0,'2021-04-20 23:37:31'),(79,111,1,'7a20ab9a62ee466ba97cec609bc405c2',0,'2021-04-21 13:51:37'),(80,111,1,'d35a9723ee9246ffaaf021808a50d066',0,'2021-04-21 21:36:49'),(81,111,1,'ff866bed06364c5cb2aac1e9a022b043',0,'2021-04-22 14:05:28'),(82,111,1,'ae4ddc4c04d34c30b1f7ae61d8c61773',0,'2021-04-22 18:11:06'),(83,111,1,'711e1215c88e455eb513651d26367b6a',0,'2021-04-23 21:17:08'),(84,111,1,'ab93ba358e9f49c5b934147feb8bc670',1,'2021-04-24 15:25:33'),(85,113,0,'f53a3e05f18d4dbb896efd9b23eebd54',1,'2021-04-24 20:40:42'),(86,111,1,'064b8b4299254aba99f0e7cd44b8c242',1,'2021-04-24 21:18:17'),(87,113,0,'49e12919505e432e91cdb23c92c3ec2e',0,'2021-04-24 21:48:51'),(88,113,0,'dc02ceacf43c45579265c07d2ec44123',1,'2021-04-24 21:49:26'),(89,111,1,'e6bdf353903544bb94a7d152381ed599',0,'2021-04-24 22:07:18'),(90,111,1,'86542611905e4feeb1b915de368f007a',1,'2021-04-26 14:35:01'),(91,111,1,'6638608f4f8b4e53a4edc627c4d9a103',0,'2021-04-26 16:08:53'),(92,111,1,'a005197f9e5647b99087ec04e453202c',1,'2021-04-27 21:36:48'),(93,111,1,'41e802b6b1f745a6842ffe2844e0533c',1,'2021-04-28 03:39:49'),(94,111,1,'0db8836355134423a0685d0bec818604',0,'2021-04-28 03:43:13'),(95,111,1,'7530e3670fe144afb9e563b91effd262',1,'2021-04-28 18:15:29'),(96,111,1,'94af30e1d3a94b5eaf3b9f5464385523',0,'2021-04-28 19:00:08'),(97,111,1,'90606751ba5449a7bc59052c76720b09',1,'2021-04-29 13:14:25'),(98,113,1,'fb63b18ab073488f89849ff547c441a7',1,'2021-04-29 14:31:56'),(99,111,1,'8137e0f579d8446d98f8cf1f8c43ee25',1,'2021-04-29 14:39:46'),(100,111,1,'e84ad48d52c149d89920d6c83ec47950',1,'2021-04-29 14:40:14'),(101,120,0,'4e752145e2694cacbb1dfec30cf9e6b8',1,'2021-04-29 14:42:19'),(102,111,1,'ec9ad61a92d14602bdfec05d655845ec',1,'2021-04-29 14:45:26'),(103,113,1,'9abb88dcd49141aa8cdd2472f72fe7de',1,'2021-04-29 14:50:32'),(104,120,0,'0862e8fe121b4d848d3cb24b6dd64610',1,'2021-04-29 17:53:08'),(105,111,1,'8e190101dbf74adfa33d546b995996f1',1,'2021-04-29 19:59:48'),(106,113,1,'0d3beb465db646f8b61f6e3514b01d66',1,'2021-04-29 20:00:27'),(107,111,1,'75eeeccfe330481c80cc06e514f47cb0',1,'2021-04-30 00:48:24'),(108,120,0,'61ec2cce6de349ecbc6c2e66372930ef',1,'2021-04-30 02:04:13'),(109,113,1,'fb93020f82554727b361d804fcce94e0',1,'2021-04-30 02:07:15'),(110,120,0,'0c65e166772a4e5eb990e790e3b7daca',1,'2021-04-30 02:07:45'),(111,113,1,'e091fb2d078e4a27b4db9716b1de8cef',0,'2021-04-30 02:08:51'),(112,113,1,'82c2c2a861b6493dbace57d82636b26b',0,'2021-04-30 15:27:27'),(113,113,1,'1490c89f75a14f91a2919d26fbb80ea8',0,'2021-05-01 19:43:54'),(114,113,1,'a8ae6193365046d197622b99ab785b1a',1,'2021-05-04 23:14:52'),(115,120,0,'00a76fbc90f744a18e828fe8e03ff088',1,'2021-05-04 23:16:00'),(116,120,0,'9e37f66b3bc14647a530ca38b777de96',1,'2021-05-04 23:50:06'),(117,111,1,'3a3dbec424604b79a899499ceed390e0',1,'2021-05-05 00:17:55'),(118,111,1,'0b140aea91d2443db19f3dd258ac291a',1,'2021-05-05 02:14:06'),(119,111,1,'5af8946e731f4571b872cbc8db6f9922',1,'2021-05-11 12:21:10'),(120,113,1,'5d7ef3b66c8b443caa9353ac21d30b09',0,'2021-05-05 09:11:24'),(121,111,1,'9306356bcfe940ff859a870a9f71a27c',0,'2021-05-05 11:40:54'),(122,113,1,'14aec9b6a10f4c0cbe4b50486ac0c8df',0,'2021-05-05 13:53:55'),(123,113,1,'12d5ece6dafb4f57a5d635180eaf34f7',0,'2021-05-06 05:15:02'),(124,113,1,'8a2a37cde2424c07aa287ecb9b5c90c8',0,'2021-05-06 08:15:45'),(125,113,1,'57e4c27d4b5d4314b7f58abc27415465',0,'2021-05-06 11:23:49'),(126,113,1,'2c47a6ef160046fab01d12b37f6c1353',0,'2021-05-06 15:01:23'),(127,113,1,'8a641ac03b6246b18fac99381bd1ceed',0,'2021-05-06 18:21:10'),(128,113,1,'2beecab9d595421090acff8514cd2cb8',1,'2021-05-14 06:18:41'),(129,121,0,'63ffa0d467d242b1ad69fe913e49f5f3',1,'2021-05-07 10:00:33'),(130,113,1,'7a152f31a5a347c8a0fbd9410c5c4542',1,'2021-05-07 10:01:29'),(131,111,1,'e21b21c4ff5e46f19b079577cb57c9d5',1,'2021-05-07 10:03:24'),(132,113,1,'38597f95a2fe401092a1793c09b2229a',1,'2021-05-07 10:36:53'),(133,111,1,'7ff442a1fa5541fd9e5d9a6a5ccc1212',1,'2021-05-07 11:25:39'),(134,113,1,'07bd5edf8b884fa6b4d73c71e056cf7d',1,'2021-05-07 11:26:10'),(135,111,1,'656fff8dfcc4405d9020f8fa43b75808',1,'2021-05-07 12:07:22'),(136,113,1,'d7d784f7c228432cb826657788cca79f',0,'2021-05-07 12:07:47'),(137,113,1,'0f4af08e0f364788b0971c1088c46a13',0,'2021-05-08 09:05:48'),(138,113,1,'30835acace9d4ea1ba7a6701df29ed52',1,'2021-05-15 09:30:54'),(139,113,1,'05dd6c3cd92841329c2770603be55ee8',1,'2021-05-09 09:49:45'),(140,120,0,'3f8f97142db64a88b18a053a6a84c425',0,'2021-05-09 10:54:06'),(141,111,1,'6245f90fcdb041b2b16dacbdc359b2bc',0,'2021-05-09 10:57:30'),(142,111,1,'f66822d35efc4ff5bcdff4f901ca8d1b',0,'2021-05-09 10:59:28'),(143,113,1,'3b7a5c0aca2448d1b6e713347fe5f918',1,'2021-05-09 11:03:37'),(144,113,1,'90415abeed5849d1b4e1432d7b357cee',1,'2021-05-09 11:14:03'),(145,120,0,'ed53a66045e542caa9073b75f54ca46a',1,'2021-05-09 11:14:29'),(146,126,0,'b00c34d76e684d55855c47cbcfefb654',1,'2021-05-09 11:25:21'),(147,113,1,'2537a0f3a43c4d679917f21e7bfb4fdc',1,'2021-05-09 11:33:02'),(148,126,0,'784b955a8b37419e9627f58baf0bc65a',1,'2021-05-09 11:41:27'),(149,127,0,'ea499ed9e62c4736892da1d405b30756',1,'2021-05-09 13:06:44'),(150,113,1,'c4c677458ced445a8da72d944a048e5d',1,'2021-05-09 13:47:16'),(151,111,1,'1f369390760b45d68949c963bc5a6a0a',1,'2021-05-09 13:48:25'),(152,113,1,'62b83c0d0bdf4c168999df6b3792f418',1,'2021-05-09 13:49:19'),(153,127,0,'382dac81f0b046bcb6bd4d8ea1d55811',1,'2021-05-09 13:56:16'),(154,113,1,'287b561e36894bee8e0d6df549411fb8',1,'2021-05-09 13:57:46'),(155,111,1,'540d685e9aa446acb947c799b723e363',0,'2021-05-09 16:39:55'),(156,113,1,'68fde7be836b4b4ba90c07342bdbacdf',1,'2021-05-10 04:21:41'),(157,120,0,'da20325f406d44cca5bd9045c2e573e0',1,'2021-05-10 06:16:54'),(158,113,1,'f076ece09d824950adeab850b8b1e171',0,'2021-05-10 06:17:32'),(159,113,1,'69e391207c95476791ee19f4452ad6b7',1,'2021-05-10 11:39:12'),(160,120,0,'d2b388a61b034bb1bdfab0bfe3b23f18',1,'2021-05-10 15:52:26'),(161,111,1,'6c26abccc74a4645b7a9a352221f3867',1,'2021-05-10 15:52:46'),(162,113,1,'03632076e84c4b7daa18476cae0e9c8e',1,'2021-05-10 15:58:32'),(163,113,1,'b5d2776a3ebb46719ec782a43e4af35f',1,'2021-05-10 15:58:55'),(164,111,1,'7ef8e2c7194049438772d062496ab870',1,'2021-05-10 15:59:19'),(165,113,1,'a56fe0b48e6b47b0a6f1f1fff0d39c74',1,'2021-05-10 16:00:30'),(166,111,1,'bfb2121e04904d56868ae1978f1183e5',1,'2021-05-10 16:05:14'),(167,120,0,'7210c8c7817943b08e44b2c528e3d06a',1,'2021-05-10 16:08:25'),(168,113,1,'007b6873a6c446a394e91ee3c5a7e19c',1,'2021-05-10 16:08:42'),(169,113,1,'1648ea59bb14486e8abe63345ad464ca',1,'2021-05-10 16:13:50'),(170,111,1,'6502c3a330e543cc816f6d20e4852769',1,'2021-05-10 16:14:13'),(171,120,0,'998264ff4c1145958eb4ee11a07da622',0,'2021-05-10 16:14:46'),(172,111,1,'740b955615c54ea099031c5496e404b6',1,'2021-05-11 10:34:58'),(173,120,0,'409ee91bcb3943dc93c66263e4f5a691',1,'2021-05-11 10:35:44'),(174,113,1,'baf27df1d98c452d95f44410448b48bd',0,'2021-05-11 10:40:23'),(175,113,1,'86830d58c966498f8fc4db08cc81c695',0,'2021-05-20 02:15:36');
/*!40000 ALTER TABLE `login_ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `my_group`
--

DROP TABLE IF EXISTS `my_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `my_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(45) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `count_member` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `my_group`
--

LOCK TABLES `my_group` WRITE;
/*!40000 ALTER TABLE `my_group` DISABLE KEYS */;
INSERT INTO `my_group` VALUES (4,'123',113,'2021-05-06 17:35:47',2),(5,'小组2',113,'2021-05-06 20:19:19',0),(6,'1',113,'2021-05-06 21:32:27',0),(7,'2',113,'2021-05-06 21:32:29',0),(8,'3',113,'2021-05-06 21:32:31',0),(9,'4',113,'2021-05-06 21:32:36',0),(10,'5',113,'2021-05-06 21:32:38',0),(11,'6',113,'2021-05-06 21:32:39',0),(12,'7',113,'2021-05-06 21:32:42',0),(13,'8',113,'2021-05-06 21:32:44',0),(14,'9',113,'2021-05-06 22:18:06',2),(15,'123',126,'2021-05-09 16:48:03',1),(16,'学习小组',127,'2021-05-09 18:43:22',0),(17,'学习小组2',127,'2021-05-09 18:43:34',1);
/*!40000 ALTER TABLE `my_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mylike`
--

DROP TABLE IF EXISTS `mylike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `mylike` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `blog_id` int(11) DEFAULT NULL,
  `question_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mylike`
--

LOCK TABLES `mylike` WRITE;
/*!40000 ALTER TABLE `mylike` DISABLE KEYS */;
INSERT INTO `mylike` VALUES (27,111,76,NULL),(31,111,77,NULL),(32,113,79,NULL),(33,111,79,NULL),(36,113,77,NULL),(37,113,83,NULL),(39,126,83,NULL),(40,126,79,NULL),(41,113,90,NULL);
/*!40000 ALTER TABLE `mylike` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paper`
--

DROP TABLE IF EXISTS `paper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `paper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `father_id` varchar(100) NOT NULL COMMENT '表示它属于什么类别',
  `title` varchar(500) NOT NULL,
  `content` longtext NOT NULL,
  `file_path` varchar(500) NOT NULL,
  `status` int(11) NOT NULL COMMENT '0-审批的paper; 1未审批的paper;2审批的代码;3未审批的代码；6审批的软件；7未审批的软件；8审批的文献；9未审批的文献；5公告；4一些说明文档',
  `gmt_create` datetime NOT NULL,
  `user_id` int(11) NOT NULL COMMENT '发布论文的用户id',
  `download_count` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paper`
--

LOCK TABLES `paper` WRITE;
/*!40000 ALTER TABLE `paper` DISABLE KEYS */;
INSERT INTO `paper` VALUES (55,'logic','连接远程服务器(1)','1','http://localhost:8080/user/file/1619423647111_8e38fc38c85249b4a61c49b41df23500.docx',0,'2021-04-26 15:54:07',111,0),(56,'logic,compute,number,algebra,geometry,topology,analysis,quantum,applied','美国页岩气压裂返排液处理技术现状及启示','1234','http://localhost:8080/user/file/1619424664175_635a8f189b3846d6a08ffc96540d0e49.pdf',2,'2021-04-26 16:11:04',111,0),(57,'logic,compute,number,algebra,geometry,topology','Luo-Chen-Book-2019','1','http://localhost:8080/user/file/1619516229505_3bc1c99df90a46bb958809c22a499bbc.pdf',0,'2021-04-27 17:37:10',111,0),(58,'logic,compute,number','Xshot-0480','1','http://localhost:8080/user/file/1619524687968_fc078a17d24b4fc29e12e960d21ca043.png',0,'2021-04-27 19:58:08',111,0),(59,'logic,compute,number','Xshot-0481','2','http://localhost:8080/user/file/1619524719935_99f04f20e29843bf98f418a3fca7ba7c.png',2,'2021-04-27 19:58:40',111,0),(60,'logic,compute,fuzzy','美国页岩气压裂返排液处理技术现状及启示','美国页岩气气压','http://localhost:8080/user/file/1619525408521_e4fb543d787f4f8d8220166efb7ac676.pdf',0,'2021-04-27 20:10:09',111,1),(61,'logic,compute,number,algebra','页岩气压裂数值模型分析','1','http://localhost:8080/user/file/1619537280887_e6f88d48eada4398b8a0db782a72f0a1.pdf',2,'2021-04-27 23:28:01',111,3),(62,'notice','公告测试','公告测试1公告测试2公告测试3公告测试公告测试5公告测试6公告测试7公告测试8公告测试9公告测试10公告测试11公告测试12公告测试13公告测试14公告测试15公告测试16公告测试17公告测试18公告测试19公告测试20','notice',5,'2021-04-29 09:49:49',111,0),(63,'notice','公告测试2','公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2公告测试2final','notice',5,'2021-04-29 10:25:44',111,0),(64,'logic,compute,number,algebra,geometry','页岩气开采技术','1','http://localhost:8080/user/file/1619664240973_64d7a76bbf2c4fcb8f137a6e4463c4d8.pdf',1,'2021-04-29 10:44:01',120,0),(65,'notice','公告测试3','公告测试3','notice',5,'2021-04-29 18:55:44',113,0),(66,'logic,compute,number,algebra','我国页岩气开采进程影响因素分析','通过对比美国采区的相关资料，分析中、美两国在成藏条件、地质条件、开采技术、管网设施、环境\r\n保护的约束等方面的差异性，系统的总结了影响页岩气开采进程的因素。结果表明我国发育的页岩类型多样、\r\n地质条件复杂且未掌握页岩气开发的核心技术、管网设施简单以及环境保护的制约等多方面的因素，是页岩气\r\n开采进展缓慢的重要因素。','http://localhost:8080/user/file/1619704135527_5d6802fad8334293a03eeb72970c7803.pdf',0,'2021-04-29 21:48:56',111,7),(67,'logic,compute,number,algebra,geometry','我国页岩气压裂开采与环境保护现状','近年来，随着美国页岩气关键技术的不断进步，\r\n引发了页岩气革命。20O9年，美国超过俄罗斯跃居\r\n为全球第一大天然气生产国。\r\n随着页岩气革命的不断推进，中国也在借鉴美国\r\n页岩气开发经验的同时积极探索。2011年12月3O\r\n日，页岩气成为新的独立矿种。2012年3月1日国土\r\n资源部公布，我国陆域页岩气地质资源潜力为\r\n134．42×10 m。，可采资源潜力为25．O8×10 m。\r\n(不含青藏地区)，分布范围涵盖上扬子及滇黔桂区、\r\n中下扬子及东南区、华北及东北区、西北区和青藏区\r\n等五大区域','http://localhost:8080/user/file/1619704172502_37ed777c6d4843e6bacba644a0ed8204.pdf',0,'2021-04-29 21:49:33',111,3),(68,'logic,compute,number,algebra,geometry','我国页岩气压裂开采与环境保护现状','1','http://localhost:8080/user/file/1619705149467_ec00571fc02e4b9da5bfc03155e02b00.pdf',1,'2021-04-29 22:05:50',120,0),(69,'logic,compute,number,algebra,geometry','Chap01绪论','1','http://localhost:8080/user/file/1619705310039_171604080ccc43abb93c36b38f83595d.pptx',3,'2021-04-29 22:08:32',120,0),(70,'logic,compute,number,geometry,topology','页岩气开采技术','1','http://localhost:8080/user/file/1619705817574_5113ca228676481d983b0674986748ef.pdf',0,'2021-04-29 22:16:58',113,7),(71,'logic,number,topology','页岩气压裂数值模型分析','1','http://localhost:8080/user/file/1619705829678_05e14eefc80a4c62b163ee4e70109973.pdf',2,'2021-04-29 22:17:10',113,1),(72,'notice','test','123','notice',5,'2021-04-30 11:54:28',113,0),(73,'logic,number,geometry,topology','页岩气压裂系统分析','123','http://localhost:8080/user/file/1620467264218_a7564254da99403f968c515d9f79c085.pdf',8,'2021-05-08 17:47:44',113,0),(74,'opsearch','美国页岩气压裂返排液处理技术现状及启示','1','http://localhost:8080/user/file/1620472438002_1710a3ebb82e4c4db2c4a0395f5eee57.pdf',8,'2021-05-08 19:13:58',113,1),(75,'opsearch','课表2','1','http://localhost:8080/user/file/1620472682540_1a56ea5002f34174a389d968213b2a5d.png',6,'2021-05-08 19:18:03',113,0),(76,'logic','数学视界说明文档','数学视界说明文档','http://localhost:8080/user/file/1620616217261_4ab39fc1001b421a99a287390fdda49b.docx',4,'2021-05-10 11:10:17',113,2),(77,'logic','数学视界管理员说明文档','数学视界管理员说明文档','http://localhost:8080/user/file/1620616237697_d6f73f49e5044d74b6203af143451519.docx',4,'2021-05-10 11:10:38',113,0);
/*!40000 ALTER TABLE `paper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `picture`
--

DROP TABLE IF EXISTS `picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `save_name` varchar(255) NOT NULL,
  `father_id` int(11) DEFAULT NULL,
  `father_type` int(11) DEFAULT NULL COMMENT '0可以抛弃，1bolg的pic，2question的pic',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `picture`
--

LOCK TABLES `picture` WRITE;
/*!40000 ALTER TABLE `picture` DISABLE KEYS */;
INSERT INTO `picture` VALUES (61,'Xshot-0475.png','1618990723084_268d4e8a0cee4166953c957799ee818d.png',75,2,'2021-04-21 15:38:43'),(62,'Xshot-0475.png','1618990747302_ab359634fb52435883129231275cb60f.png',71,1,'2021-04-21 15:39:07'),(63,'Xshot-0480.png','1619090761862_aae8dc5c9f174d5d8b097ec8538d1a6e.png',72,1,'2021-04-22 19:26:02'),(64,'Xshot-0480.png','1619426673771_5d75bf7a71ee45968771c1fa350c2798.png',74,1,'2021-04-26 16:44:34'),(67,'Xshot-0482.png','1619524783115_6a4afd0acbaa436980c9ff6f20c6bc2d.png',76,1,'2021-04-27 19:59:43'),(68,'Xshot-0475.png','1619533089137_8b354235aefa4674832cb417992101ce.png',77,1,'2021-04-27 22:18:09'),(69,'Xshot-0480.png','1619663632573_97b146b33aa8425fa16fc43834573591.png',79,1,'2021-04-29 10:33:53'),(70,'20170317151304004.gif','1619704269615_ede049a5866a4848a01402d17512ed22.gif',81,1,'2021-04-29 21:51:10'),(71,'Xshot-0480.png','1619705071036_f11a3526e7504106b9ca830ded41698f.png',82,1,'2021-04-29 22:04:31'),(75,'Xshot-0481.png','1620549832991_12cb34c849f04a55bfedd7ae2f23231f.png',88,1,'2021-05-09 16:43:53'),(78,'课表2.png','1620557990950_8f9e5ac80b924942a070bc9e6c104646.png',90,1,'2021-05-09 18:59:51'),(79,'P10513-095138.jpg','1620872246403_be5adc1aed8b4535a88c7b6c8da57abc.jpg',91,1,'2021-05-13 10:17:26');
/*!40000 ALTER TABLE `picture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `question` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `qid` varchar(200) NOT NULL COMMENT '博客id',
  `title` varchar(200) NOT NULL COMMENT '博客标题',
  `content` longtext NOT NULL COMMENT '博客内容',
  `sort` int(10) NOT NULL DEFAULT '0' COMMENT '0未解决，1已解决',
  `status` int(11) NOT NULL COMMENT '0，审批，1未审批',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `author_id` int(11) NOT NULL COMMENT '作者id',
  `author_name` varchar(200) NOT NULL COMMENT '作者名',
  `views` int(10) NOT NULL DEFAULT '0' COMMENT '浏览量',
  `author_avatar` varchar(500) DEFAULT NULL COMMENT '作者头像',
  `category_id` int(10) NOT NULL COMMENT '问题分类id',
  `category_name` varchar(50) NOT NULL COMMENT '问题分类名称',
  `gmt_update` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (37,'b32120c6f5854c1a9763b0803818c2c1','12',' 121',0,0,'2021-04-12 20:31:45',111,'asleep',1,NULL,1,'数理逻辑与数学基础','2021-04-12 20:31:45'),(41,'f85e4ba3e8184244a4744841366b4b14','321',' ewqe',0,0,'2021-04-12 22:53:11',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-12 22:53:11'),(42,'c7ce897b666347329b4657436a180741','test',' dsadsadsa',0,0,'2021-04-12 22:53:38',111,'asleep',1,NULL,1,'数理逻辑与数学基础','2021-04-12 22:53:38'),(43,'3374de22e939473bb618db83ad7a9183','删除图片测试',' dsa!',0,0,'2021-04-13 17:42:49',111,'asleep',2,NULL,1,'数理逻辑与数学基础','2021-04-13 17:42:49'),(44,'db6b20ae064445c3a726dee3ada63084','1',' 1',0,0,'2021-04-13 23:07:59',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-13 23:07:59'),(45,'9e673066b7df41f39f21ee5223dc3766','2',' 2',0,0,'2021-04-13 23:11:10',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-13 23:11:10'),(46,'f207c078437f43aab19ac28215100073','3',' ',0,0,'2021-04-13 23:11:19',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-13 23:11:19'),(47,'c895dddc7c114b4f9ec1138519aadec0','4',' 4',0,0,'2021-04-13 23:11:26',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-13 23:11:26'),(48,'c48580666cf6421990b99113c02d2ef6','5',' 5',0,0,'2021-04-13 23:11:33',111,'asleep',1,NULL,1,'数理逻辑与数学基础','2021-04-13 23:11:33'),(50,'65ca2ffacf644c218636e08afb3d064d','pic1','1',0,1,'2021-04-14 16:05:09',111,'asleep',1,NULL,1,'数理逻辑与数学基础','2021-04-14 16:05:09'),(54,'7e87a7070ed24f0580d190c2149ccbfb','12','1',0,0,'2021-04-14 16:17:02',111,'asleep',1,NULL,1,'数理逻辑与数学基础','2021-04-14 16:17:02'),(55,'0f9c39c14c4d472f82966ba4039feb37','1','2',0,0,'2021-04-14 16:58:16',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-14 16:58:16'),(56,'0db237d437c44faaae559d22de528e00','1','1',0,0,'2021-04-14 17:05:43',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-14 17:05:43'),(57,'a22448ae8c2b442fb4bb1248fdae6870','1','2',0,0,'2021-04-14 17:09:29',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-14 17:09:29'),(58,'d0164563bb424a7b91082280d26a01cf','1','1',0,0,'2021-04-14 17:11:46',111,'asleep',1,NULL,1,'数理逻辑与数学基础','2021-04-14 17:11:46'),(59,'5cafa6c8e3cb4ee5ada9f3d7ad3bf2c0','1','2',0,0,'2021-04-14 17:14:45',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-14 17:14:45'),(61,'e12005299f844adcad15c1e2e66ac8c4','1','2',0,0,'2021-04-14 17:19:15',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-14 17:19:15'),(67,'9ddab7414dfa4940a320f5381770e5b8','1','2',0,0,'2021-04-14 17:36:49',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-14 17:36:49'),(70,'ea2e2acd804c41d298003b32a63e8b07','question柱塞气举排水采气工艺工作制度研究_鲍永辉柱塞气举排水采气工艺工作制度研究_鲍永辉柱塞气举排水采气工艺工作制度研究_鲍永辉柱塞气举排水采气工艺工作制度研究_鲍永辉柱塞气举排水采气工艺工作制度研究_鲍永辉柱塞气举排水采气工艺工作制度研究_鲍永辉柱塞气举排水采气工艺工作制度研究_鲍永辉','![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1618477206014_21956174e77947e7bde8eca55c201c98.png) ',1,0,'2021-04-15 17:00:14',111,'asleep',4,NULL,1,'数理逻辑与数学基础','2021-04-15 17:00:14'),(71,'260dce658a7545a1b82ce4cada4d2810','1','![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1618921664586_b8f86740158e4d18ae2859a170b9e44b.png) ',1,0,'2021-04-20 20:31:25',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-20 20:31:25'),(75,'89018dbe281b4e6aa8dfecdeae56357a','1','![](http://localhost:8080/blog/UsingTheComplexLinkGetThePicForPicManage/1618990723084_268d4e8a0cee4166953c957799ee818d.png) ',1,0,'2021-04-21 15:38:48',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-21 15:38:48'),(76,'87eaa3ead64545268e071ac50f6638ad','数学公式test',' $$x = \\frac{-b \\pm \\sqrt{b^2-4ac}}{2a}$$',1,0,'2021-04-23 17:25:26',111,'asleep',5,NULL,1,'数理逻辑与数学基础','2021-04-23 17:25:26'),(77,'eca48b86795c4ab59a83158d17f27170','1',' 1',0,0,'2021-04-26 22:25:53',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-26 22:25:53'),(78,'a6892542b45e4a51bc7d61121e3d2ae3','1',' 1',0,0,'2021-04-26 22:45:03',111,'asleep',0,NULL,1,'数理逻辑与数学基础','2021-04-26 22:45:03'),(79,'9c5e8c6ac36c47448d6cd06adb50a9e3','1',' 2',0,0,'2021-04-27 20:00:11',111,'asleep',15,NULL,1,'数理逻辑与数学基础','2021-04-27 20:00:11'),(80,'c0dcebb97c2b4a9897fdb7edf567956d','问答test','$$ X = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}$$',1,0,'2021-04-29 10:37:32',113,'asleep1',27,NULL,1,'数理逻辑与数学基础','2021-04-29 10:37:32'),(81,'93d7f53d15de45dfb60929839f6ee5e2','1',' 1',0,0,'2021-04-29 10:43:42',120,'asleep2',7,NULL,1,'数理逻辑与数学基础','2021-04-29 10:43:42'),(82,'6df4fa7b40804f1eae608197239ff43c','非管理员测试5',' 非管理员测试5',0,1,'2021-04-29 22:05:11',120,'asleep2',3,NULL,1,'数理逻辑与数学基础','2021-04-29 22:05:11'),(83,'c0a394b615a84480a4ec794588dbcfa0','提问测试',' 提问测试:exclamation: :exclamation: :question: :question:',1,0,'2021-05-09 18:48:12',113,'asleep1',4,NULL,1,'数理逻辑与数学基础','2021-05-09 18:48:12'),(84,'884e8c2c2bf3446198b2eb8e1f2ef001','1',' 1',1,0,'2021-05-10 09:54:09',113,'asleep1',0,NULL,1,'数理逻辑与数学基础','2021-05-10 09:54:09');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL COMMENT '存储加密后的密码',
  `salt` varchar(50) DEFAULT NULL COMMENT '用于加密的随机字符串',
  `email` varchar(100) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '0-普通用户; 1-超级管理员; ',
  `status` int(11) DEFAULT NULL COMMENT '0-未激活; 1-已激活;',
  `activation_code` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_username` (`username`(20)),
  KEY `index_email` (`email`(20))
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (104,'dsad','faefa10fcb18dbc550217afda8c2b1c4','e81ac','sdadd@qq.com',0,0,'279599857f924b82af703cc7f62daf6d','2020-12-21 15:40:56'),(105,'testsdad','04da923cd0ae21887a6eb73d3f63268b','9afa9','sssssssss',0,0,'36dbc6bcc33e499dab3900841e1bd333','2020-12-22 07:34:33'),(106,'ddddddddddddddddd','92c1ab18d857a7df1fb1dca5e8b890dd','c1525','sssssssss@qq.com',0,0,'e67ccdf885e94bb5ba26ac3331b93a56','2020-12-22 07:36:27'),(107,'dsadssssssssss','709c7be98da2f757a243143698e03906','d01fa','a1sleep@sina.com',0,1,'593d37627bcb49568d23b343dc9414e9','2020-12-22 07:39:07'),(108,'ssssssssssss','b7e7bb56a4564d7070f1456b3873f388','5f298','dadsadsadsasaa@qq.com',0,0,'8a27a35793f2414e83cdb470d089d37b','2020-12-22 08:20:04'),(110,'test','hello','adsa','test@qq.com',0,1,NULL,'2020-12-24 06:24:39'),(111,'asleep','1188ca73363f4cab07ee60b7c92b5ad3','08042','60256146@qq.com',1,1,'8ef520f668004636ab5fb11c95197586','2020-12-26 08:55:12'),(113,'asleep1','8193bb798d3c3aec43e0f37606e6e38c','2f8c6','602563146@qq.com',1,1,'9fcb30cd833347a2961ef568f1911bd0','2021-01-04 10:03:22'),(114,'fck0','0efc81ba90f8d07149279fd189da0af0','3f8c5','1076991078@qq.com',1,1,'8af9d5d37eb44f55a554d77b2b500918','2021-02-21 00:15:44'),(115,'MY','5e8718c66db0e39cb2239943b8281df9','5b67e','3475901624@qq.com',0,1,'abe095b11e354770ae9c085a3504b95a','2021-01-27 11:45:42'),(117,'violet','26cd72b4550e99dcfc7ae7e4faa0f7c1','98b1e','1941050622@qq.com',0,0,'795a512ef38d4e5794f87360ba576e8b','2021-01-31 05:19:27'),(118,'fck','0efc81ba90f8d07149279fd189da0af0','3f8c5','1076991078@qq.com',1,1,'8af9d5d37eb44f55a554d77b2b500918','2021-02-20 13:51:44'),(119,'noasleep','2c61ddf1cbd869e57cac17f35a41371c','51ab7','123dsad456@qq.com',0,1,'564ded8088cf459ab528efd5fbab813e','2021-04-14 02:29:09'),(120,'asleep2','717f7de6af40b6f5aba97aa6cf8cefc7','925a9','dsadsdsad146@qq.com',0,1,'cdae06eceaef40ebbcde2f825525e356','2021-04-29 02:41:32'),(121,'asleep4','a866150a9b0eb0effbf0e3c7943fc6ce','c8aca',NULL,0,1,'2707f019af17449cb1766101098eae0e','2021-05-05 06:04:16'),(122,'asleep5','4f15732fcb10ccbe7a8dd4d8183a3184','3c4fc',NULL,0,1,'54d1191f8c6d4d4d845f4df2056e5d2d','2021-05-05 06:05:32'),(123,'asleep6','a22a67e6efb908b64f82b8ed33586950','c2809',NULL,0,1,'adba98710ebc42238935ad7147309d1d','2021-05-05 06:07:11'),(124,'asleep7','7d4e82d03acc6d970592e2a8f0a0de60','a64c1',NULL,0,1,'91afe9d7742f4093bcb2d267ab15b5e9','2021-05-05 06:09:29'),(125,'asleep8','1213038fa57ac500a0fc5f6facc7b7c3','3dd6a',NULL,0,1,'fe12d568c9c147db95aa77e6a5d30474','2021-05-05 06:10:56'),(126,'asleep9','88261d95afd94b228b20605d542a4dac','33ac2',NULL,0,1,'9c7a9c53032046cbb7716f5956ecbac0','2021-05-09 08:25:07'),(127,'测试用户','da1ca8ac42f8d63d4f64e2c2ef9346ec','d8f23',NULL,0,1,'b25f1b0cc8574699bdad95dc3da1747d','2021-05-09 10:06:27');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `nickname` varchar(80) DEFAULT NULL COMMENT '用户昵称',
  `realname` varchar(80) DEFAULT NULL COMMENT '真实姓名',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ',
  `wechat` varchar(200) DEFAULT NULL COMMENT 'WeChat',
  `email` varchar(500) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机',
  `work` varchar(200) DEFAULT NULL COMMENT '工作',
  `address` varchar(500) DEFAULT NULL COMMENT '地址',
  `hobby` varchar(500) DEFAULT NULL COMMENT '爱好',
  `intro` varchar(2000) DEFAULT NULL COMMENT '自我介绍',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,111,'aslee','王往','2132141','1234567890','321321313@qq.com','12345678','计算数学','望江','爱好1','自我介绍1'),(10,113,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'scumath_community'
--

--
-- Dumping routines for database 'scumath_community'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-09-30 19:38:36
