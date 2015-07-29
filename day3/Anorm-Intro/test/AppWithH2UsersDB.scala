import play.api.test.WithBrowser

/**
 * Created by adminuser on 7/29/15.
 */
abstract class AppWithH2UsersDB(override val app : play.api.test.FakeApplication) extends WithBrowser {

  override def around[T](t : => T)(implicit evidence$1 : org.specs2.execute.AsResult[T]) : org.specs2.execute.Result =
    super.around {
      H2TestUsersDB.createSchema()
      t
    }
}