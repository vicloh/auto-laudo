# Etapa de build usando imagem JDK 21 da Eclipse Temurin
FROM eclipse-temurin:21-jdk as build

WORKDIR /app

# Copia todo o código para o container
COPY . .

# Executa o build do projeto com Maven, gerando o uber-jar do Quarkus sem testes
RUN ./mvnw package -DskipTests -Dquarkus.package.type=uber-jar

# Etapa final, imagem runtime também com JDK 21
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia o jar gerado na etapa anterior para a imagem final
COPY --from=build /app/target/*-runner.jar app.jar

# Comando para iniciar o app
ENTRYPOINT ["java", "-jar", "app.jar"]