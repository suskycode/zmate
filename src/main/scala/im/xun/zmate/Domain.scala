package im.xun.zmate

case object TmsCheckRequest
case object TmsCheckSuccess
case object TmsCheckFail
case object TmsConfigFileUpdate

class TmsException extends Exception("Flakiness")
