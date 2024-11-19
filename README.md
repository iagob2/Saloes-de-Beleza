

# Aplicativo de Reservas para Salões de Beleza

## Descrição

Este projeto é um aplicativo de agendamento de serviços para salões de beleza e clientes, desenvolvido como parte de uma startup. O objetivo é proporcionar uma plataforma eficiente onde salões podem gerenciar seus agendamentos e clientes podem reservar horários para serviços como corte de cabelo, manicure e outros.

O aplicativo utiliza **Firebase Auth** para autenticação de usuários e **Firebase Firestore** para armazenamento de dados.

## Funcionalidades

- **Cadastro e Login**: Clientes e salões podem criar contas e fazer login com segurança utilizando Firebase Auth.
- **Gestão de Agendamentos**: Os salões podem visualizar e gerenciar os agendamentos feitos pelos clientes, incluindo a capacidade de editar ou excluir agendamentos.
- **Definir Horários e Serviços**: Os salões podem configurar seus horários de funcionamento e os serviços que oferecem.
- **Agendamento de Serviços**: Clientes podem visualizar os salões disponíveis, escolher os serviços desejados e agendar horários, com validação de disponibilidade.

## Regras de Negócio

- **RN01**: O usuário deve ser identificado como "Cliente" ou "Salão", com acesso a funcionalidades específicas.
- **RN02**: Os salões devem definir horários de funcionamento e disponibilidade de serviços.
- **RN03**: Os clientes só podem agendar serviços em horários disponíveis, sem conflitos na agenda do salão.

## Requisitos Funcionais

- **RF01**: O sistema deve permitir que clientes e salões façam login e criem contas usando Firebase Auth.
- **RF02**: O sistema deve permitir que salões definam seus horários de funcionamento e serviços oferecidos.
- **RF03**: Clientes devem poder selecionar um salão, visualizar os serviços disponíveis e agendar horários.
- **RF04**: O sistema deve armazenar dados de agendamentos, incluindo horários, serviços e status de conclusão no Firebase Firestore.

## Estrutura do Aplicativo

### 1. Tela de Login (MainActivity)

- Exibe as opções "Cadastrar" e "Entrar".
- Cadastro de cliente ou salão, com campos adicionais para salões (horário de funcionamento, dias da semana e serviços oferecidos).
- Login utilizando Firebase Auth.

### 2. Tela de Seleção de Salões (Para Clientes)

- Exibe uma lista de salões disponíveis.
- Permite ao cliente selecionar um salão e agendar um serviço.

### 3. Tela de Gestão de Agenda (Para Salões)

- O salão pode visualizar e gerenciar os agendamentos feitos pelos clientes.
- Permite editar ou excluir agendamentos e definir horários e serviços oferecidos.

### 4. Tela de Seleção de Serviço (Para Clientes)

- O cliente escolhe o serviço desejado após selecionar um salão.
- Exibe os serviços disponíveis e permite ao cliente selecionar um deles.

### 5. Tela de Seleção de Horário (Para Clientes)

- O cliente escolhe o horário disponível para o serviço selecionado.
- O sistema valida a disponibilidade e confirma o agendamento.

### 6. Tela de Histórico de Serviços (Para Clientes)

- Exibe os serviços agendados previamente pelo cliente.
- Permite visualizar o status e detalhes dos agendamentos anteriores.

## Tecnologias Utilizadas

- **Firebase Auth**: Para autenticação de usuários.
- **Firebase Firestore**: Para armazenamento de dados de agendamentos, salões e clientes.
- **RecyclerView**: Para exibir listas de salões, serviços e agendamentos.

## Como Executar o Projeto

### Pré-requisitos

1. Instale o Android Studio.
2. Baixe ou clone este repositório.
3. Configure o Firebase no projeto e adicione as credenciais necessárias.

### Passos

1. Abra o projeto no Android Studio.
2. Configure o Firebase conforme a documentação oficial do Firebase para Android.
3. Execute o aplicativo no seu dispositivo ou emulador.

## Contribuições

1. Faça um fork deste repositório.
2. Crie uma branch para a sua alteração (`git checkout -b feature/nome-da-feature`).
3. Faça o commit das suas alterações (`git commit -am 'Adiciona nova funcionalidade'`).
4. Envie para o repositório remoto (`git push origin feature/nome-da-feature`).
5. Abra um Pull Request.

## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

Se você tiver mais dúvidas ou sugestões de melhorias, fique à vontade para abrir uma issue ou enviar um pull request.

