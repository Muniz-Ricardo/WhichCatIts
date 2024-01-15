# WhichCatIts - Teste Técnico para Desenvolvedor Android Sênior

Este é um projeto de teste técnico para avaliar habilidades em desenvolvimento Android. O aplicativo "WhichCatIts" oferece funcionalidades relacionadas a gatos, como listar gatos, obter detalhes sobre um gato específico e compartilhar imagens de gatos.

## Arquitetura e Clean Code

O projeto adota a arquitetura MVVM (Model-View-ViewModel) juntamente com princípios do Clean Architecture para garantir uma separação clara de responsabilidades e facilitar a manutenção e teste do código.

### Estrutura de Pacotes

- **data**: Camada responsável pela obtenção e manipulação de dados.
- **domain**: Camada que contém as regras de negócios e modelos de domínio.
- **presentation**: Camada de interface do usuário, contendo as ViewModels, Activities e Fragments.
- **util**: Classes utilitárias reutilizáveis em todo o projeto.

#### Padrão de Branching

O projeto segue o padrão Gitflow para gerenciar o versionamento e o desenvolvimento de novas funcionalidades. Cada nova feature é desenvolvida em uma branch específica (`feat/nome_da_feature`), sendo posteriormente mesclada à branch `main` após a conclusão.

## Testes Unitários

O projeto inclui testes unitários para as classes de ViewModel, UseCase e utilitárias. A biblioteca Mockito é utilizada para a criação de mocks e simulação de comportamentos.

### Casos de uso

Importante falar que nem todos os testes possiveis foram implementados até essa atualização. A validação de algumas partes da ViewModel em **presentation** servem para garantir que algumas açoes comuns funcionem como resposta de `sucesso` do repository ou `erro`.

## Obter repo

* Clone o repositório: `git clone https://github.com/Muniz-Ricardo/WhichCatIts.git`

## Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues, sugerir melhorias ou enviar pull requests.

## Licença

Este projeto está licenciado sob a Licença MIT - consulte o arquivo [LICENSE](LICENSE) para obter detalhes.
