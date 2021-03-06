/*
 * Copyright (c) 2018, Edmund Noble
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package testz

/**
  The most boring `Harness` you can think of:
  pure tests with no resources.
  Any harness type should be convertible to a `BareHarness`;
  it's the lingua franca of tests. If you write tests using
  `BareHarness`, they can be adapted to work with any suite type later.
*/
abstract class Harness[T] {
  def test
    (name: String)
    (assertions: () => TestResult)
    : T

  def section
    (name: String)
    (test1: T, tests: T*)
    : T
}

/**
  The type of test results.
  A two-branch sum, either `Success`, or `Failure(failures)`.
 `Failure` can contain 0 or more failure messages, and/or
  throwables. The empty case of `Failure` is deliberate;
  it's the user's choice whether to add failure information.
*/
sealed abstract class TestResult

final class Failure(val failures: List[Throwable Either String]) extends TestResult {
  override def toString(): String = "Failure(\n" + failures.mkString("  ", "\n  ",  "") + "\n)"
}
object Failure {
  @inline def apply(failures: List[Throwable Either String]): TestResult =
    new Failure(failures)

  @inline def strings(failures: List[String]): TestResult =
    apply(failures.map[Throwable Either String, List[Throwable Either String]](Right(_)))

  @inline def string(failure: String): TestResult = new Failure(List(Right(failure)))

  @inline def errors(errs: Throwable*): TestResult =
    apply(errs.map[Throwable Either String, List[Throwable Either String]](Left(_))(collection.breakOut))

  @inline def error(err: Throwable): TestResult = new Failure(List(Left(err)))

  val noMessage: TestResult = new Failure(Nil)
}

case object Success extends TestResult {
  @inline final def apply(): TestResult = this
  override def toString(): String = "Success"
}
// TODO: use a pretty-printer?

object TestResult {
  def combine(first: TestResult, second: TestResult): TestResult =
    if (first eq Success) second
    else if (second eq Success) first
    else {
      Failure(first.asInstanceOf[Failure].failures ++ second.asInstanceOf[Failure].failures)
    }
}

