package folap.core.multidimensionalmodel

import scala.annotation.tailrec

/** An Attribute is a finite domain property of an Event
  */
trait Attribute:
  /** The name of the Attribute
    * @return
    *   the Attribute name
    */
  def name: String = getClass.getSimpleName

  /** The value of the Attribute
    * @return
    *   the Attribute value
    */
  def value: String

  /** The Attribute that precedes this Attribute in the hierarchy
    * @return
    *   a Some containing the parent Attribute if this Attribute is not the last
    *   in the hierarchy; None otherwise
    */
  def parent: Option[Attribute]

object Attribute:
  /** Extension methods for instances of A
    */
  extension [A <: Attribute](a: A)
    /** The hierarchy rooted in this Attribute
      * @return
      *   the list of attributes in the hierarchy in ascending order of
      *   aggregation
      */
    def hierarchy: Iterable[A] =
      @tailrec
      def recursiveHierarchy(attribute: A, acc: Iterable[A]): Iterable[A] =
        attribute.parent match
          case None => acc
          case Some(p) =>
            recursiveHierarchy(
              p.asInstanceOf[A],
              acc ++ List(p.asInstanceOf[A])
            )
      recursiveHierarchy(a, List(a))

    /** Searches the Attribute name in the hierarchy that matches one of the
      * specified names
      * @param names
      *   names to search a match with
      * @return
      *   the name of the Attribute that matches if this Attribute exists;
      *   otherwise TopAttribute
      */
    def searchCorrespondingAttributeName(names: Iterable[String]): String =
      @tailrec
      def searchInTheHierarchy(
          namesToMatch: Iterable[String],
          attribute: A
      ): String =
        if namesToMatch.isEmpty then "TopAttribute"
        else
          a.hierarchy.find(
            _.name == namesToMatch.head
          ) match
            case Some(attr) => attr.name
            case None => searchInTheHierarchy(namesToMatch.tail, attribute)
      searchInTheHierarchy(names, a)

    /** Finds the lowest common ancestor between this Attribute and the other
      * Attribute
      * @param other
      *   the Attribute to compare with
      * @return
      *   the lowest common Attribute for both attributes
      */
    def lowestCommonAncestor(other: A): A =
      @tailrec
      def findLCA(upperLevels: Iterable[A], other: A): A =
        other.hierarchy.find(_.value == upperLevels.head.value) match
          case Some(lca) => lca
          case None      => findLCA(upperLevels.tail, other)
      findLCA(a.hierarchy, other)

    /** Finds the Attribute in the hierarchy whose name matches the specified
      * one
      * @param level
      *   the name of the Attribute to match
      * @return
      *   the Attribute whose name matches the one specified
      */
    def upToLevel(level: String): A = hierarchy.find(_.name == level).get
