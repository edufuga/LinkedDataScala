package com.edufuga.scala.data.access.materialized

import com.edufuga.scala.core.Organisation
import com.edufuga.scala.data.access.ReadAll

trait MaterializingOrganisationDAO extends ReadAll[Option[Organisation]]
