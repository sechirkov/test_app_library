package com.sch.library.domain

import java.util.Date

/**
 * User: schirkov
 * Date: 9/1/2016
 */
//формуляр
case class LogBook(id: Option[Long], bookId: Long, userId: Long,
                   issuedByUser: Long, date: Date,
                   receivedByUser: Option[Long], returnDate: Option[Date]) {
}
