package com.geometry.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import com.geometry.service.Task2;
import com.geometry.entity.Angles;
import com.geometry.entity.User;
import com.geometry.ui.uiUtils.KidButton;

/**
 * 角度学习与识别界面
 */
public class AnglePanel extends JPanel {
    private MainFrame mainFrame;
    private KidButton homeButton;
    private Task2 angleTask;
    private static final String FONT_NAME = "Comic Sans MS";
    
    // 主要面板
    private JPanel inputPanel;    // 角度输入面板
    private JPanel quizPanel;     // 角度类型选择面板
    private JPanel resultPanel;   // 结果显示面板
    
    // 角度输入面板组件
    private JTextField angleInput;
    private KidButton submitAngleButton;
    private JLabel angleImageLabel;
    private JLabel instructionLabel;
    private JLabel errorLabel;
    
    // 角度类型选择面板组件
    private JLabel questionLabel;
    private JPanel optionsPanel;
    private JLabel attemptsLabel;
    private JLabel resultLabel;
    
    // 结果面板组件
    private JLabel completionLabel;
    private JLabel scoreLabel;
    private KidButton restartButton;
    
    /**
     * 构造函数
     * @param mainFrame 主窗口引用
     */
    public AnglePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.angleTask = new Task2();
        setBackground(new Color(220, 240, 255));
        initComponents();
        setupLayout();
        
        // 默认显示角度输入面板
        showInputPanel();
    }
    
    /**
     * 初始化组件
     */
    private void initComponents() {
        // 创建返回主页按钮
        homeButton = new KidButton("Home");
        homeButton.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        homeButton.setPreferredSize(new Dimension(150, 40));
        homeButton.addActionListener(e -> mainFrame.showCard(MainFrame.HOME_PANEL));
        
        // 初始化各个面板
        initInputPanel();
        initQuizPanel();
        initResultPanel();
    }
    
    /**
     * 初始化角度输入面板
     */
    private void initInputPanel() {
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false);
        
        // 角度输入指导标签
        instructionLabel = new JLabel("Type angle (0-360, x10)", SwingConstants.CENTER);
        instructionLabel.setFont(new Font(FONT_NAME, Font.BOLD, 22));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 角度输入框和提交按钮
        JPanel inputControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputControlPanel.setOpaque(false);
        angleInput = new JTextField(5);
        angleInput.setFont(new Font(FONT_NAME, Font.PLAIN, 22));
        angleInput.setPreferredSize(new Dimension(100, 40));
        angleInput.addActionListener(e -> processAngleInput());
        // 添加焦点请求，使面板显示时自动获取焦点
        angleInput.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                // 使用SwingUtilities.invokeLater确保组件完全显示后再请求焦点
                SwingUtilities.invokeLater(() -> angleInput.requestFocusInWindow());
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });
        submitAngleButton = new KidButton("Show");
        submitAngleButton.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        submitAngleButton.setPreferredSize(new Dimension(120, 40));
        submitAngleButton.addActionListener(e -> processAngleInput());
        
        inputControlPanel.add(angleInput);
        inputControlPanel.add(submitAngleButton);
        
        // 错误信息标签
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 角度图像标签
        angleImageLabel = new JLabel();
        angleImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 将组件添加到面板
        inputPanel.add(Box.createVerticalGlue());
        inputPanel.add(instructionLabel);
        inputPanel.add(Box.createVerticalStrut(20));
        inputPanel.add(inputControlPanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(errorLabel);
        inputPanel.add(Box.createVerticalStrut(20));
        inputPanel.add(angleImageLabel);
        inputPanel.add(Box.createVerticalGlue());
    }
    
    /**
     * 初始化角度类型选择面板
     */
    private void initQuizPanel() {
        quizPanel = new JPanel();
        quizPanel.setLayout(new BorderLayout(20, 20));
        quizPanel.setOpaque(false);
        
        // 问题标签
        questionLabel = new JLabel("Type?", SwingConstants.CENTER);
        questionLabel.setFont(new Font(FONT_NAME, Font.BOLD, 32));
        questionLabel.setForeground(Color.WHITE);
        
        // 选项面板 - 改为垂直布局且放在右侧
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(80, 40, 10, 80)); // 右侧加大内边距
        optionsPanel.setOpaque(false);
        
        // 尝试次数标签
        attemptsLabel = new JLabel("Tries: 3", SwingConstants.CENTER);
        attemptsLabel.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        attemptsLabel.setForeground(Color.WHITE);
        
        // 结果标签
        resultLabel = new JLabel("");
        resultLabel.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 创建左侧图像面板
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 10)); // 左侧加大内边距
        leftPanel.setOpaque(false);
        leftPanel.add(angleImageLabel, BorderLayout.CENTER);
        
        // 创建底部状态面板
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setOpaque(false);
        attemptsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusPanel.add(attemptsLabel);
        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(resultLabel);
        
        // 将组件添加到面板
        quizPanel.add(questionLabel, BorderLayout.NORTH);
        quizPanel.add(leftPanel, BorderLayout.CENTER);
        quizPanel.add(optionsPanel, BorderLayout.EAST);
        quizPanel.add(statusPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 初始化结果面板
     */
    private void initResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setOpaque(false);
        
        // 完成标签
        completionLabel = new JLabel("All done!", SwingConstants.CENTER);
        completionLabel.setFont(new Font(FONT_NAME, Font.BOLD, 38));
        completionLabel.setForeground(Color.WHITE);
        completionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 分数标签
        scoreLabel = new JLabel("You got X / X!", SwingConstants.CENTER);
        scoreLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 34));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 重新开始按钮
        restartButton = new KidButton("Again");
        restartButton.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        restartButton.setPreferredSize(new Dimension(150, 40));
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(e -> restartTask());
        
        // 将组件添加到面板
        resultPanel.add(Box.createVerticalGlue());
        resultPanel.add(completionLabel);
        resultPanel.add(Box.createVerticalStrut(20));
        resultPanel.add(scoreLabel);
        resultPanel.add(Box.createVerticalStrut(20));
        resultPanel.add(restartButton);
        resultPanel.add(Box.createVerticalGlue());
    }
    
    /**
     * 设置布局
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 顶部导航区域
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        // JLabel titleLabel = new JLabel("Angle Learning and Recognition", SwingConstants.CENTER);
        // titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, 20));
        // topPanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(homeButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // 添加顶部面板
        add(topPanel, BorderLayout.NORTH);
        
        // 主内容区域使用卡片布局，在方法中动态切换
    }
    
    /**
     * 显示角度输入面板
     */
    private void showInputPanel() {
        // 移除当前主面板内容
        Component centerComponent = ((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (centerComponent != null) {
            remove(centerComponent);
        }
        
        // 添加角度输入面板
        add(inputPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    /**
     * 显示角度类型选择面板
     */
    private void showQuizPanel() {
        // 移除当前主面板内容
        Component centerComponent = ((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (centerComponent != null) {
            remove(centerComponent);
        }
        
        // 移除图像标签，避免重复添加
        Container parent = angleImageLabel.getParent();
        if (parent != null) {
            parent.remove(angleImageLabel);
        }
        
        // 重新添加图像标签到测验面板的左侧
        JPanel leftPanel = (JPanel) quizPanel.getComponent(1); // 获取左侧面板
        leftPanel.add(angleImageLabel, BorderLayout.CENTER);
        
        // 添加角度类型选择面板
        add(quizPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    /**
     * 显示结果面板
     */
    private void showResultPanel() {
        // 移除当前主面板内容
        Component centerComponent = ((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (centerComponent != null) {
            remove(centerComponent);
        }
        
        // 更新结果标签
        // int correctCount = angleTask.getCorrectCount();
        // int requiredTypes = 4;
        scoreLabel.setText("You got these angles right!");
        
        // 添加结果面板
        add(resultPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    /**
     * 处理角度输入
     */
    private void processAngleInput() {
        try {
            int angle = Integer.parseInt(angleInput.getText().trim());
            
            // 检查角度是否在有效范围内
            if (angle < 0 || angle > 360) {
                errorLabel.setText("Angle must be between 0 and 360 degrees");
                return;
            }
            
            // 检查角度是否是10的倍数
            if (angle % 10 != 0) {
                errorLabel.setText("Angle must be a multiple of 10");
                return;
            }
            
            // 清除错误信息
            errorLabel.setText("");
            
            // 设置当前角度
            angleTask.setCurrentAngle(angle);
            
            // 显示角度图像 - 使用业务逻辑层获取图像
            ImageIcon icon = angleTask.getAngleImage(angle);
            if (icon != null) {
                // 调整图像大小以适应显示区域
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                angleImageLabel.setIcon(new ImageIcon(scaledImg));
            } else {
                // 如果找不到图片，则使用原来的方法绘制
                icon = createAngleIcon(angle, 300, 300);
                angleImageLabel.setIcon(icon);
                System.err.println("Warning: Falling back to dynamic angle drawing for angle " + angle);
            }
            
            // 切换到测验面板
            setupQuizOptions();
            updateAttemptsLabel();
            resultLabel.setText("");
            showQuizPanel();
            
        } catch (NumberFormatException e) {
        }
    }
    
    /**
     * 设置角度类型选择选项
     */
    private void setupQuizOptions() {
        // 清空选项面板
        optionsPanel.removeAll();
        
        // 获取所有角度类型
        java.util.List<String> angleTypes = angleTask.getAngleTypes();
        
        // 为每种角度类型创建按钮
        for (String type : angleTypes) {
            String typeName = angleTask.getAngleTypeName(type);
            KidButton optionButton = new KidButton(typeName);
            optionButton.setFont(new Font(FONT_NAME, Font.BOLD, 20));
            optionButton.setPreferredSize(new Dimension(220, 50));
            optionButton.setMaximumSize(new Dimension(220, 50));
            optionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            optionButton.addActionListener(e -> checkAngleType(type));
            optionsPanel.add(optionButton);
            optionsPanel.add(Box.createVerticalStrut(15));
        }
        
        // 重新布局选项面板
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }
    
    /**
     * 检查用户回答
     */
    private void checkAngleType(String selectedType) {
        boolean isCorrect = angleTask.checkAnswer(selectedType);
        
        if (isCorrect) {
            // 计算得分
            int points = User.calScores("Basic", 3 - angleTask.getRemainingAttempts() + 1);
            
            // 创建得分提示弹窗
            JDialog scoreDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Score", true);
            scoreDialog.setLayout(new BorderLayout(10, 10));
            scoreDialog.setSize(400, 220);
            scoreDialog.setLocationRelativeTo(this);
            
            // 创建得分面板
            JPanel scorePanel = new JPanel(new BorderLayout(10, 10));
            scorePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            // 添加得分信息
            JLabel scoreLabel = new JLabel("Score + " + points + " !", SwingConstants.CENTER);
            scoreLabel.setFont(new Font(FONT_NAME, Font.BOLD, 28));
            scoreLabel.setForeground(new Color(0, 150, 0));
            
            // 确认按钮
            KidButton okButton = new KidButton("OK");
            okButton.setFont(new Font(FONT_NAME, Font.BOLD, 20));
            okButton.addActionListener(e -> scoreDialog.dispose());
            scoreDialog.getRootPane().setDefaultButton(okButton);
            
            // 组装面板
            scorePanel.add(scoreLabel, BorderLayout.CENTER);
            scorePanel.add(okButton, BorderLayout.SOUTH);
            
            scoreDialog.add(scorePanel);
            
            // 更新结果标签
            resultLabel.setText("Great!");
            resultLabel.setForeground(new Color(0, 150, 0));
            
            // 显示得分弹窗
            scoreDialog.setVisible(true);
            
            // 延迟显示下一个问题
            Timer timer = new Timer(500, e -> {
                if (angleTask.isTaskCompleted()) {
                    mainFrame.updateTaskStatus("Angles", true);
                    showResultPanel();
                } else {
                    showInputPanel();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            resultLabel.setText("Try again!");
            resultLabel.setForeground(Color.WHITE);
            
            if (angleTask.getRemainingAttempts() <= 0) {
                // 显示正确答案
                resultLabel.setText("Answer: " + angleTask.getCurrentAngleType());
                Timer timer = new Timer(2000, e -> {
                    if (angleTask.isTaskCompleted()) {
                        mainFrame.updateTaskStatus("Angles", true);
                        showResultPanel();
                    } else {
                        showInputPanel();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
        
        // 更新尝试次数显示
        updateAttemptsLabel();
    }
    
    /**
     * 更新尝试次数标签
     */
    private void updateAttemptsLabel() {
        int remaining = angleTask.getRemainingAttempts();
        attemptsLabel.setText("Tries: " + remaining);
    }
    
    /**
     * 重新开始任务
     */
    private void restartTask() {
        // 重新初始化任务
        angleTask = new Task2();
        
        // 清空角度输入框
        angleInput.setText("");
        errorLabel.setText("");
        
        // 重置图像标签
        angleImageLabel.setIcon(null);
        
        // 显示角度输入面板
        showInputPanel();
    }
    
    /**
     * 创建角度图标
     */
    private ImageIcon createAngleIcon(int degrees, int width, int height) {
        // 创建一个缓冲图像
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 设置背景为白色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        
        // 计算中心点和半径
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = (int) (Math.min(width, height) / 1.5);
        
        // 设置线条颜色和宽度
        g2d.setColor(new Color(70, 130, 180)); // 深蓝色箭头
        g2d.setStroke(new BasicStroke(3));
        
        String angleType = angleTask.getAngleType(degrees);
        
        // 计算第二条线的角度 - 注意：在Java图形坐标系中，0度在东方，90度在南方
        // 所以我们需要使用 -degrees 来正确绘制角度
        double radians = Math.toRadians(-degrees);
        
        if (angleType.equals(Task2.ACUTE_ANGLE)) {
            // 锐角 (< 90°)
            // 绘制第一条线 (水平向右)
            g2d.drawLine(centerX - radius / 2, centerY, centerX + radius, centerY);
            
            // 绘制第二条线 (向上倾斜)
            int startX = centerX - radius / 2;
            int startY = centerY;
            int endX = centerX - radius / 2 + (int)(radius * Math.cos(radians));
            int endY = centerY + (int)(radius * Math.sin(radians));
            g2d.drawLine(startX, startY, endX, endY);
            
            // 绘制箭头
            drawArrow(g2d, centerX + radius, centerY, 0);
            drawArrow(g2d, endX, endY, radians);
            
            // 绘制角度标记
            g2d.setColor(new Color(200, 220, 250, 150)); // 浅蓝色填充，半透明
            g2d.fillArc(centerX - radius / 2 - radius / 4, centerY - radius / 4, 
                        radius / 2, radius / 2, 0, degrees);
            
        } else if (angleType.equals(Task2.RIGHT_ANGLE)) {
            // 直角 (90°)
            // 绘制水平线
            g2d.drawLine(centerX - radius / 2, centerY, centerX + radius, centerY);
            
            // 绘制垂直线
            g2d.drawLine(centerX - radius / 2, centerY, centerX - radius / 2, centerY + radius);
            
            // 绘制箭头
            drawArrow(g2d, centerX + radius, centerY, 0);
            drawArrow(g2d, centerX - radius / 2, centerY + radius, -Math.PI/2);
            
            // 绘制直角标记 (小正方形)
            g2d.setColor(new Color(200, 220, 250, 150)); // 浅蓝色填充，半透明
            int squareSize = radius / 6;
            g2d.fillRect(centerX - radius / 2, centerY, squareSize, squareSize);
            
        } else if (angleType.equals(Task2.OBTUSE_ANGLE)) {
            // 钝角 (> 90° 且 < 180°)
            // 绘制第一条线 (水平向右)
            g2d.drawLine(centerX, centerY, centerX + radius, centerY);
            
            // 绘制第二条线 (向下倾斜)
            int endX = centerX + (int)(radius * Math.cos(radians));
            int endY = centerY + (int)(radius * Math.sin(radians));
            g2d.drawLine(centerX, centerY, endX, endY);
            
            // 绘制箭头
            drawArrow(g2d, centerX + radius, centerY, 0);
            drawArrow(g2d, endX, endY, radians);
            
            // 绘制角度弧
            g2d.setColor(new Color(200, 220, 250, 150)); // 浅蓝色填充，半透明
            g2d.fillArc(centerX - radius / 4, centerY - radius / 4, 
                       radius / 2, radius / 2, 0, degrees);
            
        } else if (angleType.equals(Task2.STRAIGHT_ANGLE)) {
            // 平角 (180°)
            // 绘制水平线
            g2d.drawLine(centerX - radius, centerY, centerX + radius, centerY);
            
            // 绘制箭头
            drawArrow(g2d, centerX - radius, centerY, Math.PI);
            drawArrow(g2d, centerX + radius, centerY, 0);
            
            // 绘制中点标记
            g2d.setColor(new Color(200, 220, 250)); // 浅蓝色填充
            int dotSize = 6;
            g2d.fillOval(centerX - dotSize/2, centerY - dotSize/2, dotSize, dotSize);
            
            // 绘制角度弧
            g2d.setColor(new Color(200, 220, 250, 150));
            g2d.drawArc(centerX - radius / 4, centerY - radius / 4, radius / 2, radius / 2, 0, 180);
            
        } else if (angleType.equals(Task2.REFLEX_ANGLE)) {
            // 优角 (> 180° 且 < 360°)
            // 绘制第一条线 (水平向右)
            g2d.drawLine(centerX, centerY, centerX + radius * 3/4, centerY);
            
            // 绘制第二条线 (顺时针方向)
            int endX = centerX + (int)(radius * 3/4 * Math.cos(radians));
            int endY = centerY + (int)(radius * 3/4 * Math.sin(radians));
            g2d.drawLine(centerX, centerY, endX, endY);
            
            // 绘制箭头
            drawArrow(g2d, centerX + radius * 3/4, centerY, 0);
            drawArrow(g2d, endX, endY, radians);
            
            // 绘制角度弧
            g2d.setColor(new Color(200, 220, 250, 150)); // 浅蓝色填充，半透明
            g2d.fillArc(centerX - radius / 4, centerY - radius / 4, 
                       radius / 2, radius / 2, 0, degrees);
        }
        
        // 清理资源
        g2d.dispose();
        
        return new ImageIcon(image);
    }
    
    /**
     * 在线段末端绘制箭头
     */
    private void drawArrow(Graphics2D g2d, int x, int y, double angle) {
        int arrowSize = 10;
        
        // 保存当前颜色
        Color originalColor = g2d.getColor();
        
        // 计算箭头的两个点
        int x1 = (int) (x - arrowSize * Math.cos(angle - Math.PI/6));
        int y1 = (int) (y + arrowSize * Math.sin(angle - Math.PI/6));
        int x2 = (int) (x - arrowSize * Math.cos(angle + Math.PI/6));
        int y2 = (int) (y + arrowSize * Math.sin(angle + Math.PI/6));
        
        // 绘制箭头
        g2d.setColor(originalColor);
        int[] xPoints = {x, x1, x2};
        int[] yPoints = {y, y1, y2};
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // 恢复原始颜色
        g2d.setColor(originalColor);
    }
}