package at.diplomarbeit.studybuddy.exception;

public class InseratNotFoundException extends RuntimeException {
    public InseratNotFoundException(Long id) {
        super("Inserat" + id + "konnte nicht gefunden werden.");
    }
}
