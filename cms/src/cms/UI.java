package cms;

import java.util.Scanner;
import java.util.List;

public class UI {
    private Scanner sc = new Scanner(System.in);
    private AuthenticationHSQLDB authHSQLDB = new AuthenticationHSQLDB();
    private ContentHSQLDB contentHSQLDB = new ContentHSQLDB();
    private User currentUser = null;

    public void start() {
    	
        if (authHSQLDB.listUsers().isEmpty()) {
            System.out.println("Nenhum usuário encontrado. Por favor, crie o primeiro usuário.");
            createUser();
        }
        
        while (true) {
            System.out.println("Menu Inicial:");
            System.out.println("1. Login: Fazer login no sistema.");
            System.out.println("2. Listar Conteúdos: Listar os conteúdos.");
            System.out.println("3. Sair: Sair do sistema.");
            System.out.print("Escolha uma opção: ");
            int option = sc.nextInt();
            sc.nextLine(); // Consume newline

            if (option == 1) {
                currentUser = authLogin(new User());
                if (currentUser != null) {
                    contentMenu();
                }
            } else if (option == 2) {
                listContent();
            } else if (option == 3) {
                System.out.println("Saindo do sistema...");
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private User authLogin(User user) {
        System.out.print("Digite o nome de usuário: ");
        String username = sc.nextLine();
        user.setUsername(username);
        System.out.print("Digite a senha: ");
        String password = sc.nextLine();
        user.setPassword(password);

        if (authHSQLDB.auth(user)) {
            System.out.println("Login bem-sucedido.");
            return user;
        } else {
            System.out.println("Login falhou.");
            return null;
        }
    }

    private void contentMenu() {
        int option = 0;

        while (option != 10) {
            System.out.println("Menu após Login:");
            System.out.println("1. Criar Conteúdo: Cria novo conteúdo.");
            System.out.println("2. Listar Conteúdo: Listar os conteúdos.");
            System.out.println("3. Atualizar Conteúdo: Editar conteúdo.");
            System.out.println("4. Excluir Conteúdo: Deletar conteúdo.");
            System.out.println("5. Criar Usuário: Cria novo Usuário.");
            System.out.println("6. Listar Usuário: Listar os usuários.");
            System.out.println("7. Alterar Usuário: Edita o Usuário.");
            System.out.println("8. Excluir Usuário: Deletar Usuário.");
            System.out.println("9. Alterar Senha: Permite ao usuário alterar a própria senha.");
            System.out.println("10. Logout: Sai do login.");
            System.out.print("Escolha uma opção: ");
            option = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    createContent();
                    break;
                case 2:
                    listContent();
                    break;
                case 3:
                    updateContent();
                    break;
                case 4:
                    deleteContent();
                    break;
                case 5:
                    createUser();
                    break;
                case 6:
                    listUsers();
                    break;
                case 7:
                    updateUser();
                    break;
                case 8:
                    deleteUser();
                    break;
                case 9:
                    changePassword();
                    break;
                case 10:
                    System.out.println("Logout bem-sucedido.");
                    currentUser = null;
                    return; // Logout
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void createUser() {
        System.out.print("Digite o nome de usuário: ");
        String username = sc.nextLine();
        System.out.print("Digite a senha: ");
        String password = sc.nextLine();
        User newUser = new User(username, password);
        authHSQLDB.createUser(newUser);
        System.out.println("Usuário criado com sucesso: " + username);
    }

    private void listUsers() {
        List<User> users = authHSQLDB.listUsers();
        System.out.println("Listando usuários:");
        for (User user : users) {
            System.out.println("Username: " + user.getUsername());
        }
    }

    private void updateUser() {
        System.out.print("Digite o nome do usuário para atualizar: ");
        String username = sc.nextLine();
        System.out.print("Digite a nova senha: ");
        String password = sc.nextLine();
        User user = new User(username, password);
        if (authHSQLDB.updateUser(user)) {
            System.out.println("Usuário atualizado: " + username);
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    private void deleteUser() {
        System.out.print("Digite o nome do usuário para excluir: ");
        String username = sc.nextLine();
        if (authHSQLDB.deleteUser(username)) {
            System.out.println("Usuário excluído: " + username);
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    private void changePassword() {
        System.out.print("Digite a nova senha: ");
        String newPassword = sc.nextLine();
        if (authHSQLDB.changePassword(currentUser, newPassword)) {
            System.out.println("Senha alterada com sucesso.");
        } else {
            System.out.println("Erro ao alterar senha.");
        }
    }
          
    private void createContent() {
        System.out.print("Digite o ID do conteúdo: ");
        String id = sc.nextLine();
        System.out.print("Digite o texto do conteúdo: ");
        String text = sc.nextLine();
        Content content = new Content(id, text);
        contentHSQLDB.save(content);
        System.out.println("Conteúdo criado: " + id);
    }

    private void listContent() {
        List<Content> contents = contentHSQLDB.list();
        System.out.println("Listando conteúdos:");
        for (Content content : contents) {
            System.out.println("ID: " + content.getId() + ", Texto: " + content.getText());
        }
    }

    private void updateContent() {
        System.out.print("Digite o ID do conteúdo para atualizar: ");
        String id = sc.nextLine();
        System.out.print("Digite o novo texto do conteúdo: ");
        String text = sc.nextLine();
        Content content = new Content(id, text);
        contentHSQLDB.update(content);
        System.out.println("Conteúdo atualizado: " + id);
    }

    private void deleteContent() {
        System.out.print("Digite o ID do conteúdo para excluir: ");
        String id = sc.nextLine();
        boolean removed = contentHSQLDB.remove(id);
        if (removed) {
            System.out.println("Conteúdo excluído: " + id);
        } else {
            System.out.println("Conteúdo não encontrado: " + id);
        }
    }
}
