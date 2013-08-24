/**
 * Copyright (c) 2002-2013 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher

import org.scalatest.Assertions
import org.neo4j.cypher.internal.commands.expressions.Identifier
import org.neo4j.cypher.internal.ExecutionContext
import org.junit.{Ignore, Test}
import org.neo4j.cypher.internal.commands.Equals
import org.neo4j.cypher.internal.commands.values.IsUnknown

class TernaryPredicateLogicTest extends Assertions {

  @Test @Ignore
  def should_evaluate_equal_of_unknowns_as_unknown() {
    // given
    val ctx = ExecutionContext.from("a" -> IsUnknown, "b" -> IsUnknown)

    // when
    val predicate = Equals(Identifier("a"), Identifier("b"))

    // then
    assert( IsUnknown === predicate(ctx)(null) )
  }
}