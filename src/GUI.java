
public class GUI extends Client {

    public void loginScreen() {
        new LoginScreen(this, this::mainScreen);
    }

    public void mainScreen() {
        new ChatAppGUI(this, this.getUserAccount());
    }
}
