package objsets

import PostReader.*

/**
 * A class to represent posts.
 */
class Post(val user: String, val text: String, val reposts: Int):
  override def toString: String =
    "User: " + user + "\n" +
    "Text: " + text + " [" + reposts + "]"

/**
 * This represents a set of objects of type `Post` in the form of a binary search
 * tree. Every branch in the tree has two children (two `PostSet`s). There is an
 * invariant which always holds: for every branch `b`, all elements in the left
 * subtree are smaller than the post at `b`. The elements in the right subtree are
 * larger.
 *
 * Note that the above structure requires us to be able to compare two posts (we
 * need to be able to say which of two posts is larger, or if they are equal). In
 * this implementation, the equality / order of posts is based on the post's text
 * (see `def incl`). Hence, a `PostSet` could not contain two posts with the same
 * text from different users.
 *
 *
 * The advantage of representing sets as binary search trees is that the elements
 * of the set can be found quickly. If you want to learn more you can take a look
 * at the Wikipedia page [1], but this is not necessary in order to solve this
 * assignment.
 *
 * [1] http://en.wikipedia.org/wiki/Binary_search_tree
 */
abstract class PostSet extends PostSetInterface:

  /**
   * This method takes a predicate and returns a subset of all the elements
   * in the original set for which the predicate is true.
   *
   * Question: Can we implement this method here, or should it remain abstract
   * and be implemented in the subclasses?
   */
  def filter(p: Post => Boolean): PostSet = filterAcc(p, new Empty)

  /**
   * This is a helper method for `filter` that propagates the accumulated posts.
   */
  def filterAcc(p: Post => Boolean, acc: PostSet): PostSet

  /**
   * Returns a new `PostSet` that is the union of `PostSet`s `this` and `that`.
   *
   * Question: Should we implement this method here, or should it remain abstract
   * and be implemented in the subclasses?
   */
  def union(that: PostSet): PostSet

  /**
   * Returns the post from this set which has the greatest repost count.
   *
   * Calling `mostReposted` on an empty set should throw an exception of
   * type `java.util.NoSuchElementException`.
   *
   * Question: Should we implement this method here, or should it remain abstract
   * and be implemented in the subclasses?
   */
  def mostReposted: Post

  /**
   * Returns a list containing all posts of this set, sorted by repost count
   * in descending order. In other words, the head of the resulting list should
   * have the highest repost count.
   *
   * Hint: the method `remove` on PostSet will be very useful.
   * Question: Should we implement this method here, or should it remain abstract
   * and be implemented in the subclasses?
   */
  def descendingByRepost: PostList

  /**
   * The following methods are already implemented
   */

  /**
   * Returns a new `PostSet` which contains all elements of this set, and the
   * the new element `post` in case it does not already exist in this set.
   *
   * If `this.contains(post)`, the current set is returned.
   */
  def incl(post: Post): PostSet

  /**
   * Returns a new `PostSet` which excludes `post`.
   */
  def remove(post: Post): PostSet

  /**
   * Tests if `post` exists in this `PostSet`.
   */
  def contains(post: Post): Boolean

  /**
   * This method takes a function and applies it to every element in the set.
   */
  def foreach(f: Post => Unit): Unit

class Empty extends PostSet:
  def filterAcc(p: Post => Boolean, acc: PostSet): PostSet = acc

  /**
   * The following methods are already implemented
   */

  def contains(post: Post): Boolean = false

  def incl(post: Post): PostSet = NonEmpty(post, Empty(), Empty())

  def remove(post: Post): PostSet = this

  def foreach(f: Post => Unit): Unit = ()

  /**
   * The following methods added for this assignment
   */

  def union(that: PostSet): PostSet = that

  override def mostReposted: Post = throw new NoSuchElementException
  override def descendingByRepost: PostList = Nil


class NonEmpty(elem: Post, left: PostSet, right: PostSet) extends PostSet:

  def filterAcc(p: Post => Boolean, acc: PostSet): PostSet = {
    val leftAcc = left.filterAcc(p, acc)
    val rightAcc = right.filterAcc(p, leftAcc)
    if p(elem) then rightAcc.incl(elem) else rightAcc
  }


  /**
   * The following methods are already implemented
   */

  def contains(x: Post): Boolean =
    if x.text < elem.text then
      left.contains(x)
    else if elem.text < x.text then
      right.contains(x)
    else true

  def incl(x: Post): PostSet =
    if x.text < elem.text then
      NonEmpty(elem, left.incl(x), right)
    else if elem.text < x.text then
      NonEmpty(elem, left, right.incl(x))
    else
      this

  def remove(tw: Post): PostSet =
    if tw.text < elem.text then
      NonEmpty(elem, left.remove(tw), right)
    else if elem.text < tw.text then
      NonEmpty(elem, left, right.remove(tw))
    else
      left.union(right)

  def foreach(f: Post => Unit): Unit =
    f(elem)
    left.foreach(f)
    right.foreach(f)

    /**
     * The following methods are already implemented
     */

  def union(that: PostSet): PostSet = {
    left.union(right.union(that.incl(elem)))
  }

  override def mostReposted: Post = {
    def compare(post: Post, set: PostSet): Post = {
      try {
        val mostRep = set.mostReposted
        if (post.reposts > mostRep.reposts) post else mostRep
      } catch {
        case _: NoSuchElementException => post
      }
    }
    compare(compare(elem, left), right)
  }

  override def descendingByRepost: PostList = {
    val most = mostReposted
    new Cons(most, remove(most).descendingByRepost)
  }

trait PostList:
  def head: Post
  def tail: PostList
  def isEmpty: Boolean
  def foreach(f: Post => Unit): Unit =
    if !isEmpty then
      f(head)
      tail.foreach(f)

object Nil extends PostList:
  def head = throw java.util.NoSuchElementException("head of EmptyList")
  def tail = throw java.util.NoSuchElementException("tail of EmptyList")
  def isEmpty = true

class Cons(val head: Post, val tail: PostList) extends PostList:
  def isEmpty = false


object GoogleVsApple:
  val google = List("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus")
  val apple = List("ios", "iOS", "iphone", "iPhone", "ipad", "iPad")

  lazy val googlePosts: PostSet = PostReader.allPosts.filter(post => google.exists(kwd => post.text.contains(kwd)))
  lazy val applePosts: PostSet = PostReader.allPosts.filter(post => apple.exists(kwd => post.text.contains(kwd)))

  /**
   * A list of all posts mentioning a keyword from either apple or google,
   * sorted by the number of reposts.
   */
  lazy val trending: PostList = (googlePosts union applePosts).descendingByRepost

@main
def main(): Unit = {
  // Print the trending posts
  GoogleVsApple.trending foreach println
}

