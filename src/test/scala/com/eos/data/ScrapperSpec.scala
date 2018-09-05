package com.eos.data

import org.scalatest.{FlatSpec, MustMatchers, PrivateMethodTester}
import play.api.libs.json.{JsString, JsValue, Json}

class ScrapperSpec extends FlatSpec with MustMatchers with PrivateMethodTester{

  val historyEvent = "\u001B[32m2018-09-05T16:51:55.840 thread-0   chain_plugin.cpp:636          operator()           ] HISTORY_TRACES 169 {\"id\":\"0a662c0b0ff98f851ca8b9dad2fd8d858be5cbbe9d65224d7997c93f2d87eaf7\",\"receipt\":{\"status\":\"executed\",\"cpu_usage_us\":491,\"net_usage_words\":25},\"elapsed\":224,\"net_usage\":200,\"scheduled\":false,\"action_traces\":[{\"receipt\":{\"receiver\":\"eosio\",\"act_digest\":\"d70e3e73d015bce6c2484b4bcf494d32b1a867aaabefb0a7d75329870f8f5c87\",\"global_sequence\":180,\"recv_sequence\":180,\"auth_sequence\":[[\"eosio\",180]],\"code_sequence\":1,\"abi_sequence\":1},\"act\":{\"account\":\"eosio\",\"name\":\"newaccount\",\"authorization\":[{\"actor\":\"eosio\",\"permission\":\"active\"}],\"data\":{\"creator\":\"eosio\",\"name\":\"eosio.unregd\",\"owner\":{\"threshold\":1,\"keys\":[{\"key\":\"EOS7EarnUhcyYqmdnPon8rm7mBCTnBoot6o7fE2WzjvEX2TdggbL3\",\"weight\":1}],\"accounts\":[],\"waits\":[]},\"active\":{\"threshold\":1,\"keys\":[{\"key\":\"EOS7EarnUhcyYqmdnPon8rm7mBCTnBoot6o7fE2WzjvEX2TdggbL3\",\"weight\":1}],\"accounts\":[],\"waits\":[]}},\"hex_data\":\"0000000000ea30559098ba5303ea305501000000010003350529efca8c607421e95846cc2a3d2efaa8454018deb75a204e27acf29ee5cc0100000001000000010003350529efca8c607421e95846cc2a3d2efaa8454018deb75a204e27acf29ee5cc01000000\"},\"elapsed\":54,\"cpu_usage\":0,\"console\":\"\",\"total_cpu_usage\":0,\"trx_id\":\"0a662c0b0ff98f851ca8b9dad2fd8d858be5cbbe9d65224d7997c93f2d87eaf7\",\"inline_traces\":[]}],\"except\":null}\u001B[0m"
  val validJsValue = Json.parse("{\"id\":\"0a662c0b0ff98f851ca8b9dad2fd8d858be5cbbe9d65224d7997c93f2d87eaf7\",\"receipt\":{\"status\":\"executed\",\"cpu_usage_us\":491,\"net_usage_words\":25},\"elapsed\":224,\"net_usage\":200,\"scheduled\":false,\"action_traces\":[{\"receipt\":{\"receiver\":\"eosio\",\"act_digest\":\"d70e3e73d015bce6c2484b4bcf494d32b1a867aaabefb0a7d75329870f8f5c87\",\"global_sequence\":180,\"recv_sequence\":180,\"auth_sequence\":[[\"eosio\",180]],\"code_sequence\":1,\"abi_sequence\":1},\"act\":{\"account\":\"eosio\",\"name\":\"newaccount\",\"authorization\":[{\"actor\":\"eosio\",\"permission\":\"active\"}],\"data\":{\"creator\":\"eosio\",\"name\":\"eosio.unregd\",\"owner\":{\"threshold\":1,\"keys\":[{\"key\":\"EOS7EarnUhcyYqmdnPon8rm7mBCTnBoot6o7fE2WzjvEX2TdggbL3\",\"weight\":1}],\"accounts\":[],\"waits\":[]},\"active\":{\"threshold\":1,\"keys\":[{\"key\":\"EOS7EarnUhcyYqmdnPon8rm7mBCTnBoot6o7fE2WzjvEX2TdggbL3\",\"weight\":1}],\"accounts\":[],\"waits\":[]}},\"hex_data\":\"0000000000ea30559098ba5303ea305501000000010003350529efca8c607421e95846cc2a3d2efaa8454018deb75a204e27acf29ee5cc0100000001000000010003350529efca8c607421e95846cc2a3d2efaa8454018deb75a204e27acf29ee5cc01000000\"},\"elapsed\":54,\"cpu_usage\":0,\"console\":\"\",\"total_cpu_usage\":0,\"trx_id\":\"0a662c0b0ff98f851ca8b9dad2fd8d858be5cbbe9d65224d7997c93f2d87eaf7\",\"inline_traces\":[]}],\"except\":null}")
  val nonNewAccountActionTrace = Json.parse("{\"account\":\"eosio\",\"name\":\"onblock\",\"authorization\":[{\"actor\":\"eosio\",\"permission\":\"active\"}],\"data\":\"82fa5c450000000000ea30550000000000a7c9e6eeca5bd73a896a362542884ca75a3aba8eeec3080b25ba8b456d00000000000000000000000000000000000000000000000000000000000000008e302109a6a81ca86afe33829c8d7b0d5df688fed88274a1126dda25ba841ef0000000000000\"}")
  val newAccountActionTrace = Json.parse("{\"account\":\"eosio\",\"name\":\"newaccount\",\"authorization\":[{\"actor\":\"eosio\",\"permission\":\"active\"}],\"data\":{\"creator\":\"eosio\",\"name\":\"eosio.unregd\",\"owner\":{\"threshold\":1,\"keys\":[{\"key\":\"EOS7EarnUhcyYqmdnPon8rm7mBCTnBoot6o7fE2WzjvEX2TdggbL3\",\"weight\":1}],\"accounts\":[],\"waits\":[]},\"active\":{\"threshold\":1,\"keys\":[{\"key\":\"EOS7EarnUhcyYqmdnPon8rm7mBCTnBoot6o7fE2WzjvEX2TdggbL3\",\"weight\":1}],\"accounts\":[],\"waits\":[]}},\"hex_data\":\"0000000000ea30559098ba5303ea305501000000010003350529efca8c607421e95846cc2a3d2efaa8454018deb75a204e27acf29ee5cc0100000001000000010003350529efca8c607421e95846cc2a3d2efaa8454018deb75a204e27acf29ee5cc01000000\"}")
  val validOptionalAccount = OptionalAccount(Some(JsString("eosio")),Some(JsString("newaccount")),Some(JsString("eosio")),Some(JsString("eosio.unregd")))
  val invalidOptionalAccount = OptionalAccount(Some(JsString("eosio")),Some(JsString("onblock")), None, None)
  val validAccount = Account("eosio","newaccount","eosio","eosio.unregd")

  "validEvent" should "return false for events without HISTORY_TRACES string" in {
    val validEvent = PrivateMethod[Boolean]('validEvent)
    val result = Scrapper invokePrivate validEvent("sdlkfjsdlkfjldsjf {skdjfldsjf }")
    result mustBe false
  }

  "validEvent" should "return true for events with HISTORY_TRACES string" in {
    val validEvent = PrivateMethod[Boolean]('validEvent)
    val result = Scrapper invokePrivate validEvent(historyEvent)
    result mustBe true
  }

  "extractJsString" should "return correct Json object string" in {
    val extractJsString = PrivateMethod[String]('extractJsString)
    val result = Scrapper invokePrivate extractJsString(historyEvent)
    result mustBe validJsValue.toString()
  }

  "extractActionTraces" should "return list of action_traces object" in {
    val extractActionTraces = PrivateMethod[Seq[JsValue]]('extractActionTraces)
    val result = Scrapper invokePrivate extractActionTraces(validJsValue)
    result.size mustBe 1
    result.head mustBe newAccountActionTrace
  }

  "extractOptionalAcountInfo" should "return OptionalAccount object when act is of newaccount type" in {
    val extractOptionalAcountInfo = PrivateMethod[OptionalAccount]('extractOptionalAcountInfo)
    val result = Scrapper invokePrivate extractOptionalAcountInfo(newAccountActionTrace)
    result mustBe validOptionalAccount
  }

  "extractOptionalAcountInfo" should "return OptionalAccount object when act is not of newaccount type" in {
    val extractOptionalAcountInfo = PrivateMethod[OptionalAccount]('extractOptionalAcountInfo)
    val result = Scrapper invokePrivate extractOptionalAcountInfo(nonNewAccountActionTrace)
    result mustBe invalidOptionalAccount
  }

  "extractNewAcountInfo" should "return Account object from OptionalAccount object" in {
    val extractNewAcountInfo = PrivateMethod[Account]('extractNewAcountInfo)
    val result = Scrapper invokePrivate extractNewAcountInfo(validOptionalAccount)
    result mustBe validAccount
  }
}
