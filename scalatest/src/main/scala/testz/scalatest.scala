
package testz

import scala.concurrent.Future

// import org.specs2.mutable
// import org.specs2.specification.core.Fragment

object scalatest {

  abstract class SpecsSuite[T] {
    def test(test: () => Future[TestResult]): T
    def section(tests1: T, tests: T*): T
  }

  // abstract class TaskSuite() extends mutable.Specification {
  //   def tests[T[_]](test: Harness[() => ?, T]): T[Unit]

  //   private def makeHarness: Harness[() => ?, ? => Fragment] =
  //     new Harness[() => ?, ? => Fragment] {
  //       def apply[R]
  //         (name: String)
  //         (assertion: R => () => TestResult)
  //         : R => Fragment = {
  //         // todo: catch exceptions
  //         r => name in (assertion(r) must_== Success)
  //       }
  //       def section[R]
  //         (name: String)
  //         (
  //           test1: R => Fragment,
  //           tests: R => Fragment*
  //         ): R => Fragment = { r =>
  //           name should {
  //             val h = test1(r)
  //             tests.map(_(r)).lastOption.getOrElse(h)
  //           }
  //       }
  //       def bracket[R, I]
  //         (init: () => I)
  //         (cleanup: I => () => Unit)
  //         (tests: ((I, R)) => Fragment
  //       ): R => Fragment = { r =>
  //         val i = init()
  //         // this probably doesn't work, because
  //         // specs2 has some magical execution model things.
  //         val f = tests((i, r))
  //         cleanup(i)
  //         f
  //       }
  //       def mapResource[R, RN](test: R => Fragment)(f: RN => R): RN => Fragment = {
  //         rn => test(f(rn))
  //       }
  //   }

  //   test[? => Fragment](makeHarness)(())

  // }

}
