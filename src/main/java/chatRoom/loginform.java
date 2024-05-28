package chatRoom;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Objects;

public class loginform {
    private JPanel panel1;
    private JButton loginButton;
    private JTextField accountTxt;
    private JTextField pswTxt;
    private JLabel account;
    private JLabel psw;
    private JFrame frame;


    public loginform() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mobile = accountTxt.getText();
                String name = pswTxt.getText();
                if (mobile.isEmpty() || mobile.trim().equals("")) {
                    System.out.println("手机号为空");
                    JOptionPane.showMessageDialog(null, "手机号不能为空");
                    return;
                }
                if (name.isEmpty() || name.trim().equals("")) {
                    System.out.println("姓名为空");
                    JOptionPane.showMessageDialog(null, "姓名不能为空");
                    return;
                }
                String url = "http://chatroom.codingpython.cn/login?mobile=" + mobile + "&name=" + name;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    String content = null;
                    if (response.body() != null) {
                        content = response.body().string();
                    }
                    System.out.println(content);
                    Gson gson = new Gson();
                    Map responseJson = gson.fromJson(content, Map.class);
                    Object detail = responseJson.get("detail");
                    if (detail != null) {
                        String detailStr =  detail.toString();
                        JOptionPane.showMessageDialog(null,detailStr );
                        return;
                    }
                    Object token = responseJson.get("token");
                    if (token != null) {
                        frame.dispose();
                        String tokenToString = token.toString();
                        lobby lobby = new lobby();
                        lobby.run(tokenToString);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void run() {

        frame = new JFrame("loginform");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new loginform().run();
    }
}
