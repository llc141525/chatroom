package chatRoom;

import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class lobby {
    private JPanel panel1;
    private JTextField messageField;
    private JButton 发送Button;
    private JButton javaButton;
    private JButton foodButton;
    private JButton graduateButton;
    private JList messageList;
    private String token;
    private DefaultListModel messageListModel;
    private Frame frame;

    public lobby() {
        发送Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = messageField.getText();
                OkHttpClient client = new OkHttpClient();
                String sendUrl = "http://chatroom.codingpython.cn/chatroom/chat?token=" + token + "&room=Lobby" + "&message=" + text;
                if (!text.equals("") ) {
                    Request request = new Request.Builder().url(sendUrl).build();
                    try {
                        Response response = client.newCall(request).execute();
                        String content = response.body().string();

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                refreashMessageList(token);
                messageField.setText("");
            }
        });
        javaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomName = javaButton.getText();
                room room = new room();
                room.run(token, roomName);
            }
        });
        foodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomName = foodButton.getText();
                room room = new room();
                room.run(token, roomName);
            }
        });
        graduateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomName = graduateButton.getText();
                room room = new room();
                room.run(token, roomName);
            }
        });
    }

    public void run(String token) {
        this.token = token;

        JFrame frame = new JFrame("lobby");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        messageListModel = new DefaultListModel();
        messageList.setModel(messageListModel);
        refreashMessageList(token);

    }

    private void refreashMessageList(String token) {
        OkHttpClient client = new OkHttpClient();
        String LobbyUrl = "http://chatroom.codingpython.cn/chatroom/messages?token=" + token + "&room=Lobby";
        Request request = new Request.Builder().url(LobbyUrl).build();
        try {
            Response response = client.newCall(request).execute();
            String content = response.body().string();
            System.out.println(content);
            Gson gson = new Gson();
            Map map = gson.fromJson(content, Map.class);
            List<Map> messages = (List<Map>) map.get("messages");
            if (messages != null) {
                messageListModel.clear();
            }
            for (Map map1 : messages) {
                System.out.println(map1.get("name"));
                System.out.println(map1.get("message"));
                messageListModel.addElement(MessageFormat.format("({0})-{1}: {2}", map1.get("created_at"), map1.get("name"), map1.get("message")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) {
//        lobby lb = new lobby();
//        String token = "6cfd385db86e479d9ba3fe4b4e20fc50";
//        lb.run(token);
//        lb.refreashMessageList(token);
//
//    }
}
