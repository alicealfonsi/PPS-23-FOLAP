package folap.core

import scala.annotation.tailrec

/** The model for representing and querying data in a DW
  */
object MultidimensionalModel:

  /** An Attribute is a finite domain property of an Event
    */
  trait Attribute[L]:
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
      *   a Some containing the parent Attribute if this Attribute is not the
      *   last in the hierarchy; None otherwise
      */
    def parent: Option[Attribute[L]]

    /** Indicates whether this Attribute is "equal to" the other Attribute
      * @param other
      *   the Attribute with which to compare
      * @return
      *   true if this Attribute has the same name and value as the other; false
      *   otherwise
      */
    def equals(other: Attribute[L]): Boolean =
      level == other.level && value == other.value

    /** Get the current hierarchy level
      *
      * @return
      *   the level
      */
    def level: L

  object Attribute:
    extension [L, A <: Attribute[L]](a: A)
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
      def searchCorrespondingAttributeName(names: Iterable[L]): L =
        @tailrec
        def searchInTheHierarchy(
            namesToMatch: Iterable[L],
            attribute: A
        ): L =
          if namesToMatch.isEmpty then ???
          else
            a.hierarchy.find(
              _.level == namesToMatch.head
            ) match
              case Some(attr) => attr.level
              case None => searchInTheHierarchy(namesToMatch.tail, attribute)
        searchInTheHierarchy(names, a)

      /** Finds the Attribute in the hierarchy whose name matches the specified
        * one
        * @param level
        *   the name of the Attribute to match
        * @return
        *   the Attribute whose name matches the one specified
        */
      def upToLevel(level: L): A = hierarchy.find(_.level == level).get

  /** A Measure represents a numeric value associated with an Event
    */
  trait Measure:
    /** Abstract type member representing the type of the Measure
      */
    type T <: MeasureType

    /** The name of the Measure
      * @return
      *   the Measure name
      */
    def name: String = getClass.getSimpleName

    /** The value of the Measure
      * @return
      *   the Measure value
      */
    def value: T
