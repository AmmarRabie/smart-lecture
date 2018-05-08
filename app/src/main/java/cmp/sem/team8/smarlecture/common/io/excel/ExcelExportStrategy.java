package cmp.sem.team8.smarlecture.common.io.excel;

import android.os.Environment;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cmp.sem.team8.smarlecture.common.data.model.FileModel;
import cmp.sem.team8.smarlecture.common.data.model.NoteModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;
import cmp.sem.team8.smarlecture.common.io.ExportStrategy;
import cmp.sem.team8.smarlecture.common.io.ExportTask;

/**
 * Created by AmmarRabie on 30/04/2018.
 */

public class ExcelExportStrategy implements ExportStrategy {
    private static final String TAG = "ExcelExportStrategy";

    @Override
    public ExportTask exportOneFile(FileModel fileData, String fileName) {
        // make the fileName the group name + curr day date
        if (fileName == null) {
            fileName = fileData.getGroup().getName();
            DateFormat df = new SimpleDateFormat("ddMMyy_HHmmss", Locale.US);
            fileName += " " + df.format(new Date());
        }
        try {
            Workbook workbook = new HSSFWorkbook(); // HSSFWorkbook for generating `.xls` file

            createSheet(workbook, fileData);
            // Write the output to a file
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(path, "/" + fileName + ".xls");

            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            // Closing the workbook
            workbook.close();
            return new ExportTask(true, "success");
        } catch (IOException e) {
            e.printStackTrace();
            return new ExportTask(false, "can't make the file, check your permissions");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new ExportTask(false, "Not supported in this device");
        } catch (Exception e) {
            e.printStackTrace();
            return new ExportTask(false, null);
        }
    }


    @Override
    public ExportTask exportFiles(List<FileModel> filesData) {
        return null;
    }

    private Sheet createSheet(Workbook workbook, FileModel sheetData) throws ClassNotFoundException {
        // Create a Sheet
        Sheet sheet = workbook.createSheet(sheetData.getGroup().getName());
        ArrayList<String> columns = createSheetColumns(sheetData.getSessions());
        createHeaderRow(sheet, workbook, columns);

        CellStyle memberNameCellStyle = makeStyleForNameCell(workbook);
        int startFromRow = 1; // from one because of first row header
        for (int iRow_member = 0; iRow_member < sheetData.getMembers().size(); iRow_member++) {
            FileModel.GroupMember currGroupMember = sheetData.getMembers().get(iRow_member);
            Row currRow = sheet.createRow(startFromRow++);

            // enter the name of this member
            Cell memberNameCell = currRow.createCell(0);
            memberNameCell.setCellStyle(memberNameCellStyle);
            memberNameCell.setCellValue(currGroupMember.getName());

            // write the sessions data of this member
            fillSessionsDataForMember(workbook,
                    currRow,
                    sheetData.getSessions(),
                    currGroupMember,
                    1);


            // finally, put the grade
            currRow.createCell(columns.size() - 1).setCellValue(currGroupMember.getAttendanceGrade());
        }
        return sheet;
    }


    /**
     * Fill sessions data for one member
     *
     * @param workbook            workbook holding the sheet
     * @param memberRow           the row of this member in the sheet
     * @param groupSessions       all sessions this group has
     * @param member              the member data of the row
     * @param shiftLeftCellsCount 0 means that the sessions data will start from the most left cell
     *                            generally this always passed 1 because of first column header
     */
    private void fillSessionsDataForMember(Workbook workbook,
                                           Row memberRow,
                                           ArrayList<SessionModel> groupSessions,
                                           FileModel.GroupMember member,
                                           int shiftLeftCellsCount) {

        // loop over all sessions group
        // if the session exists in member sessions (mean that this user was in the group while session is not-activated)
        // if true fill the data of ( attend, notes )
        // else put the '-' char

        ArrayList<FileModel.SessionMember> memberInSessions = member.getInSessions();

        // loop over all group sessions
        for (int iGroupSession = 0; iGroupSession < groupSessions.size(); iGroupSession++) {
            String currGroupSessionId = groupSessions.get(iGroupSession).getId();
            FileModel.SessionMember currSessionData = findSessionById(memberInSessions, currGroupSessionId);
            Cell currCell = memberRow.createCell(iGroupSession + shiftLeftCellsCount);

            if (currSessionData == null) // the user doesn't exist in this group session
            {
                currCell.setCellValue("--");
                continue;
            }

            // write his notes and attend

            CellStyle attendCellStyle = makeAttendanceCellStyle(workbook, currSessionData.isAttend());
            currCell.setCellStyle(attendCellStyle);
            ArrayList<NoteModel> notes = currSessionData.getNotes();
            currCell.setCellValue("");
            for (NoteModel note : notes) {
                currCell.setCellValue(currCell.getStringCellValue() + note.getText() + '\n');
            }
            if (notes.size() == 0)
                currCell.setCellValue("NO notes !");
        }
    }

    private CellStyle makeAttendanceCellStyle(Workbook workbook, boolean attend) {
        CellStyle style = workbook.createCellStyle();
        if (attend)
            style.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        else
            style.setFillBackgroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        return style;
    }

    private CellStyle makeStyleForNameCell(Workbook workbook) {
        Font NameFont = workbook.createFont();

        NameFont.setBold(true);
        NameFont.setFontHeightInPoints((short) 14);
        NameFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle NameCellStyle = workbook.createCellStyle();
        NameCellStyle.setFont(NameFont);
        NameCellStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        return NameCellStyle;
    }

    private ArrayList<String> createSheetColumns(ArrayList<SessionModel> sessions) {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("member-session");
        for (int i = 0; i < sessions.size(); i++) {
            columns.add(sessions.get(i).getName());
        }
        columns.add("attendance-grade");
        return columns;
    }

    private void createHeaderRow(Sheet sheet, Workbook workbook, ArrayList<String> columns) {
        Font headerFont = workbook.createFont();

        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());

        // Create a Row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue(columns.get(0));

        // Creating cells
        for (int i = 1; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i));
            cell.setCellStyle(headerCellStyle);
        }
    }

    /**
     * @param sessions main list of sessions to search in it for the id.
     * @param target   the id caller want to search for it in the sessions list
     * @return return the value that matches 2 ids, null if it doesn't find target in sessions
     */
    private FileModel.SessionMember findSessionById(ArrayList<FileModel.SessionMember> sessions, String target) {
        for (FileModel.SessionMember sessionMember : sessions)
            if (sessionMember.getSessionId().equals(target))
                return sessionMember;
        return null;
    }
}
