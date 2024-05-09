package objsets

object PostReader:

  object ParsePosts:
    def regexParser(s: String): List[Map[String, Any]] =
      // In real life. you would use an actual JSON library...
      val postRegex = """^\{ .*"user": "([^"]+)", "text": "([^"]+)", "reposts": ([\\.0-9]+) \},?$""".r
      s.split("\r?\n").toList.tail.init.map {
        case postRegex(user, text, reposts) => Map("user" -> user, "text" -> text, "reposts" -> reposts.toDouble)
      }

    def getPosts(user: String, json: String): List[Post] =
      for map <- regexParser(json) yield
        val text = map("text")
        val reposts = map("repost_count")
        Post(user, text.toString, reposts.toString.toDouble.toInt)

    def getPostData(user: String, json: String): List[Post] =
      // is list
      val l = regexParser(json)
      for map <- l yield
        val text = map("text")
        val reposts = map("reposts")
        Post(user, text.toString, reposts.toString.toDouble.toInt)

  def toPostSet(l: List[Post]): PostSet =
    l.foldLeft(Empty(): PostSet)(_.incl(_))

  def unparseToData(tws: List[Post]): String =
    val buf = StringBuffer()
    for tw <- tws do
      val json = "{ \"user\": \"" + tw.user + "\", \"text\": \"" +
                                    tw.text.replaceAll(""""""", "\\\\\\\"") + "\", \"reposts\": " +
                                    tw.reposts + ".0 }"
      buf.append(json + ",\n")
    buf.toString

  val sites = List("gizmodo", "TechCrunch", "engadget", "amazondeals", "CNET", "gadgetlab", "mashable")

  private val gizmodoPosts = PostReader.ParsePosts.getPostData("gizmodo", PostData.gizmodo)
  private val techCrunchPosts = PostReader.ParsePosts.getPostData("TechCrunch", PostData.TechCrunch)
  private val engadgetPosts = PostReader.ParsePosts.getPostData("engadget", PostData.engadget)
  private val amazondealsPosts = PostReader.ParsePosts.getPostData("amazondeals", PostData.amazondeals)
  private val cnetPosts = PostReader.ParsePosts.getPostData("CNET", PostData.CNET)
  private val gadgetlabPosts = PostReader.ParsePosts.getPostData("gadgetlab", PostData.gadgetlab)
  private val mashablePosts = PostReader.ParsePosts.getPostData("mashable", PostData.mashable)

  private val sources = List(gizmodoPosts, techCrunchPosts, engadgetPosts, amazondealsPosts, cnetPosts, gadgetlabPosts, mashablePosts)

  val postMap: Map[String, List[Post]] =
    Map() ++ Seq((sites(0) -> gizmodoPosts),
                 (sites(1) -> techCrunchPosts),
                 (sites(2) -> engadgetPosts),
                 (sites(3) -> amazondealsPosts),
                 (sites(4) -> cnetPosts),
                 (sites(5) -> gadgetlabPosts),
                 (sites(6) -> mashablePosts))

  val postSets: List[PostSet] = sources.map(posts => toPostSet(posts))

  private val sitePostSetMap: Map[String, PostSet] =
    Map() ++ (sites zip postSets)

  private def unionOfAllPostSets(curSets: List[PostSet], acc: PostSet): PostSet =
    if curSets.isEmpty then
      acc
    else
      unionOfAllPostSets(curSets.tail, acc.union(curSets.head))

  val allPosts: PostSet = unionOfAllPostSets(postSets, Empty())

