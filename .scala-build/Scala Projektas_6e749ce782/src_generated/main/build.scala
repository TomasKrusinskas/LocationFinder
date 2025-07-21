

final class build$_ {
def args = build_sc.args$
def scriptPath = """build.sc"""
/*<script>*/
// build.sc — Scala CLI build config

//> using scala "3.3.1"

// JSON library
//> using dep "com.lihaoyi::upickle:4.2.1"

// Testing framework
//> using dep "org.scalameta::munit::0.7.29"

/*</script>*/ /*<generated>*//*</generated>*/
}

object build_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new build$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    val _ = script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export build_sc.script as `build`

