package com.sch.library.dao

import slick.driver.JdbcProfile

/**
 * User: schirkov
 * Date: 9/8/2016
 */

trait DBComponent {
    val driver: JdbcProfile

    import driver.api._

    val db: Database
}
