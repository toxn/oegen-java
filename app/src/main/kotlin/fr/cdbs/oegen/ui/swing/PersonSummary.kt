package fr.cdbs.oegen.ui.swing

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.ui.PersonList
import java.util.EventListener
import java.util.EventObject
import javax.swing.BoxLayout
import javax.swing.JTextPane
import javax.swing.event.HyperlinkEvent
import javax.swing.event.HyperlinkListener
import javax.swing.text.html.HTMLDocument

class PersonSummary : PersonRelationalComponent(), HyperlinkListener {
    class PersonClickEvent(source: Any?, val person: Person?) : EventObject(source)

    fun interface PersonClickListener : EventListener {
        fun personClick(pce: PersonClickEvent)
    }

    private val listeners = ArrayList<PersonClickListener>()
    private val textPane = JTextPane()

    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        textPane.isEditable = false
        textPane.contentType = "text/html"

        (textPane.document as HTMLDocument).styleSheet
            .addRule("." + Person.PROPERTY_LASTNAME + " { font-variant: small-caps; }")

        textPane.addHyperlinkListener(this)
        add(textPane)
        rebuild()
    }

    fun addPersonClickListener(pcl: PersonClickListener) {
        listeners.add(pcl)
    }

    override fun hyperlinkUpdate(e: HyperlinkEvent) {
        if (e.eventType == HyperlinkEvent.EventType.ACTIVATED) {
            firePersonClick(Person.persons.search(e.description))
        }
    }

    fun removePersonClickListener(pcl: PersonClickListener) {
        listeners.remove(pcl)
    }

    private fun firePersonClick(person: Person?) {
        val evt = PersonClickEvent(this, person)
        for (pcl in listeners) {
            pcl.personClick(evt)
        }
    }

    override fun rebuild() {
        val currentCenter = center ?: return

        textPane.text = buildString {
            append("<html>")
            append("  <head>")
            append("   <title>An example HTMLDocument</title>")
            append("   </head>")
            append("   <body>")
            append("       <h1>")
            append(personToHtml(currentCenter, link = false))
            append("       </h1>")
            append("     <ul>")
            append("       <li>Parents")
            append("         <ul>")
            append(personToHtmlList(currentCenter.father))
            append(personToHtmlList(currentCenter.mother))
            append("         </ul")
            append("       </li>")
            append("       <li>Siblings")
            append("         <ul>")
            append(personListToHtmlList(currentCenter.siblings))
            append("         </ul>")
            append("       </li>")
            append("       <li>Children")
            append("         <ul>")
            append(personListToHtmlList(currentCenter.children))
            append("         </ul>")
            append("       </li>")
            append("     </ul>")
            append("   </body>")
            append("</html>")
        }
    }

    companion object {
        private fun personListToHtmlList(children: PersonList?): String {
            if (children == null) return ""
            return children.joinToString("") { personToHtmlList(it) }
        }

        private fun personToHtml(person: Person?, link: Boolean): String {
            if (person == null) return ""

            var result = "<span class=\"" + Person.PROPERTY_GENDER + "\">" + person.gender!!.symbol +
                "</span> " +
                "<span class=\"" + Person.PROPERTY_LASTNAME + "\" id=\"" + Person.PROPERTY_LASTNAME + "\">" +
                person.lastName + "</span> " +
                "<span class=\"" + Person.PROPERTY_FIRSTNAME + "\">" +
                person.firstName + "</span>"

            if (link) {
                result = "<a href=\"" + person.id + "\">" + result + "</a"
            }

            return result
        }

        private fun personToHtmlList(person: Person?): String {
            if (person == null) return ""
            return "<li>" + personToHtml(person, link = true) + "</li"
        }
    }
}
