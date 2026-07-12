package fr.cdbs.oegen.ui.print

import fr.cdbs.oegen.data.Person
import java.awt.print.PrinterException
import java.awt.print.PrinterJob
import javax.swing.JOptionPane
import fr.cdbs.oegen.ui.Messages

object PrintActions {
    fun printTree(center: Person?, generations: Int) {
        if (center == null) return
        val job = PrinterJob.getPrinterJob()
        job.jobName = Messages.getString("Action.Person.Relations.Print.Tree.JobName")
        job.setPrintable(PersonTreePrintable(center, generations))
        if (!job.printDialog()) return
        try {
            job.print()
        } catch (ex: PrinterException) {
            JOptionPane.showMessageDialog(
                null,
                ex.message ?: ex.toString(),
                Messages.getString("DialogTitle.Error"),
                JOptionPane.ERROR_MESSAGE,
            )
        }
    }

    fun printSummary(person: Person?, summaryGenerations: Int) {
        if (person == null) return
        val job = PrinterJob.getPrinterJob()
        job.jobName = Messages.getString("Action.Person.Relations.Print.Detail.JobName")
        job.setPrintable(PersonSummaryPrintable(person, summaryGenerations))
        if (!job.printDialog()) return
        try {
            job.print()
        } catch (ex: PrinterException) {
            JOptionPane.showMessageDialog(
                null,
                ex.message ?: ex.toString(),
                Messages.getString("DialogTitle.Error"),
                JOptionPane.ERROR_MESSAGE,
            )
        }
    }
}
