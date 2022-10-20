package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddStudCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ToggleViewCommand;
import seedu.address.logic.commands.ViewAllCommand;
import seedu.address.logic.commands.ViewClassCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Class;
import seedu.address.model.student.ClassPredicate;
import seedu.address.model.student.Id;
import seedu.address.model.student.IdPredicate;
import seedu.address.model.student.Name;
import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.model.student.NamePredicate;
import seedu.address.model.student.Student;
import seedu.address.testutil.EditStudentDescriptorBuilder;
import seedu.address.testutil.StudentBuilder;
import seedu.address.testutil.StudentUtil;

public class StudentRecordParserTest {

    private final StudentRecordParser parser = new StudentRecordParser();

    @Test
    public void parseCommand_add() throws Exception {
        Student person = new StudentBuilder().build();
        AddStudCommand command = (AddStudCommand) parser.parseCommand(StudentUtil.getAddCommand(person));
        assertEquals(new AddStudCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        Student student = new StudentBuilder().build();
        Id studentId = student.getId();
        Name studentName = student.getStudentName();

        DeleteCommand idCommand = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " id/" + studentId);
        assertEquals(new DeleteCommand(studentId, new IdPredicate(studentId)), idCommand);

        DeleteCommand nameCommand = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " nm/" + studentName);
        assertEquals(new DeleteCommand(studentName, new NamePredicate(studentName)), nameCommand);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Student person = new StudentBuilder().build();
        EditCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + StudentUtil.getEditStudentDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_viewByName() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " nm/" + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_viewById() throws Exception {
        FindCommand command = (FindCommand) parser.parseCommand(FindCommand.COMMAND_WORD + " id/ 123a");
        assertEquals(new FindCommand(new IdPredicate(new Id("123A"))), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_viewAll() throws Exception {
        assertTrue(parser.parseCommand(ViewAllCommand.COMMAND_WORD) instanceof ViewAllCommand);
        assertTrue(parser.parseCommand(ViewAllCommand.COMMAND_WORD + " 3") instanceof ViewAllCommand);
    }

    @Test
    public void parseCommand_toggleView() throws Exception {
        assertTrue(parser.parseCommand(ToggleViewCommand.COMMAND_WORD) instanceof ToggleViewCommand);
        assertTrue(parser.parseCommand(ToggleViewCommand.COMMAND_WORD + " 3") instanceof ToggleViewCommand);
    }

    @Test
    public void parseCommand_viewClass() throws Exception {
        ViewClassCommand viewClassCommand = (ViewClassCommand) parser.parseCommand(
                ViewClassCommand.COMMAND_WORD + " 3A");
        assertEquals(new ViewClassCommand(new ClassPredicate(new Class("3A"))), viewClassCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
