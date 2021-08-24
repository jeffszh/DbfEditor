package cn.jeff.test

object TestJdbc {

	@JvmStatic
	fun main(args: Array<String>) {
		println("開始")
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver")
	}

}
