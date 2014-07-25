import sbt._                                                
import Keys._                                               

object TmsBuild extends Build {                           

  val newSetting = SettingKey[String]("deploy-dir", "zmate")

  val myTask = TaskKey[State]("my-task")                  

  val mySettings = Seq(                                   
    newSetting := "default",                              
    myTask <<= (state, newSetting) map { (state, newSetting) =>  
    println("newSetting: " + newSetting)                
    state
  }
  )

  lazy val root =
    Project(id = "hello",
      base = file("."),
      settings = Project.defaultSettings ++ mySettings)            
  }
