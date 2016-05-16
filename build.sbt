androidBuild

name := "aarca"

version       := "0.2"

scalaVersion  := "2.11.8"

javacOptions in Compile ++= "-source" :: "1.7" :: "-target" :: "1.7" :: Nil

scalacOptions in Compile ++= Seq("-optimise", "-Xlint", "-unchecked", "-deprecation", "-encoding", "utf8", "-feature")


//useProguard in Android := false
//
//useProguardInDebug in Android := false
//
//proguardScala in Android := false

//dexMulti in Android := true

//dexMinimizeMain in Android := true

//dexMaxProcessCount := 1

showSdkProgress in Android := false


//dexMainClasses in Android := Seq(
//    "gie/aarca/MainActivity.class"
//)


//proguardOptions in Android ++= Seq(
//    "-dontwarn org.scalatest.**",
//    "-dontwarn org.mockito.**",
//    "-dontwarn org.objenesis.**",
//    "-dontnote org.scalatest.**",
//    "-dontnote org.mockito.**",
//    "-dontnote org.objenesis.**",
//    "-keep class * extends org.scalatest.FunSuite"
//)

//proguardCache in Android ++=
//        "org.scaloid" ::
//        "org.scalatest" ::
//        "org.scalautils" ::
//        "org.mockito" ::
//        "scala.reflect" ::
//        "scala.xml" ::
//        "scala.util.parsing.combinator" ::
//        "scala.util.parsing.input" ::
//        "scala.util.parsing.json" ::
//        Nil

packagingOptions in Android := PackagingOptions(excludes = Seq(
    "META-INF/MANIFEST.MF",
    "META-INF/LICENSE.txt",
    "META-INF/LICENSE",
    "META-INF/NOTICE.txt",
    "META-INF/NOTICE"
))



//Logging

libraryDependencies ++= {
    val sloggingV = "0.4.0"
    Seq(
        //"org.slf4j" % "slf4j-android" % "1.7.21",
        "biz.enef" %% "slogging" % "0.4.0",
        "biz.enef" %% "slogging-slf4j" % "0.4.0"
    )
}