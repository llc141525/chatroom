package chatRoom;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sun.plugin2.message.Message;

import javax.swing.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class room {
    private JPanel panel1;
    private JButton 发送Button;
    private JTextField textField1;
    private JList list1;
    private JLabel room_name;
    private static final String ROOM_URL = "http://chatroom.codingpython.cn/chatroom/messages?token={0}&room={1}";
    private static final String SEND_URL = "http://chatroom.codingpython.cn/chatroom/chat?token={0}&room={1}&message={2}";
    private String token;
    private String room;
    private DefaultListModel listModel;


    public room() {
        发送Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField1.getText();
                if (!text.equals("")) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(MessageFormat.format(SEND_URL, token, room, text)).build();
                    try {
                        Response response = client.newCall(request).execute();
                        String content = response.body().string();
                        System.out.println(content);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                refreashMessage();
                textField1.setText("");
            }
        });
    }

    private void refreashMessage() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(MessageFormat.format(ROOM_URL, token, room)).build();
        System.out.println(MessageFormat.format(ROOM_URL, token, room));
        System.out.println(room);
        System.out.println(token);
        try {
            Response response = client.newCall(request).execute();
            String content = response.body().string();
//            System.out.println(content);
            Gson gson = new Gson();
            Map messages = gson.fromJson(content, Map.class);
            List<Map> list = (List<Map>) messages.get("messages");
            System.out.println(list);
            if (listModel != null) {
                listModel.clear();
            }
            for (Map message : list) {
                System.out.println(message);
                listModel.addElement(MessageFormat.format("({0})-{1}: {2}", message.get("created_at"), message.get("name"), message.get("message")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(String token, String room) {
        JFrame frame = new JFrame("room");
        this.room = room;
        this.token = token;
        room_name.setText(room);
        frame.setTitle(room);
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        listModel = new DefaultListModel();
        list1.setModel(listModel);
        refreashMessage();
    }

//    public static void main(String[] args) {
//        String token = "6cfd385db86e479d9ba3fe4b4e20fc50";
//        room room = new room();
//        room.run(token, "台院美食");
//    }
}
