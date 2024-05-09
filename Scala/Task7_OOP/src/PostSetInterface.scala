package objsets

/**
 * The interface used by the grading infrastructure. Do not change signatures
 * or your submission will fail with a NoSuchMethodError.
 */
trait PostSetInterface:
  def incl(post: Post): PostSet
  def remove(post: Post): PostSet
  def contains(post: Post): Boolean
  def foreach(f: Post => Unit): Unit
  def union(that: PostSet): PostSet
  def mostReposted: Post
  def descendingByRepost: PostList

