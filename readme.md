### 利用Spring Aop实现控制方法并发执行个数

#### 1. 生成baseline配置文件
```shell
./gradlew baselineUpdateConfig 
```

#### 2. 生成ipr
```shell
./gradlew cleanIdea idea
```

#### 3. 代码检查
```shell
./gradlew baselineCheck
```
#### 4. 自动格式化代码，确保项目代码符合一致的代码格式。这个任务会根据预定义的风格对 Java 代码进行自动格式化。
```shell
./gradlew baselineFormat
```

#### 5. 自动格式化代码，确保项目代码符合一致的代码格式。这个任务会根据预定义的风格对 Java 代码进行自动格式化。
```shell
./gradlew baselineFormat
```

#### 6.检查项目中是否有不必要或过时的依赖。会帮助您清理未使用的依赖，减少项目的冗余和体积。
```shell
./gradlew baselineDependencies
```

#### 7. 用于自动化版本管理，确保项目遵循一致的版本控制规则。它会自动设置版本号并更新项目的版本。

```shell
./gradlew baselineVersion
```

#### 8. 自动发布项目，将构建的 artefact（如 JAR 文件）发布到一个 Maven 仓库。
```shell
./gradlew baselinePublish
```

#### 9. 用于执行发布任务并更新版本号，适用于发布新版本的项目。执行完该任务后，版本号将自动更新，并准备好发布到 Maven 中央仓库或其他仓库。
```shell
./gradlew baselineRelease
```

#### 10. 集成了 Error Prone，这是一个静态分析工具，用于检查代码中的潜在错误并提供警告。
```shell
  ./gradlew baselineErrorProne
```

#### 11. 自动更新项目的依赖版本，确保所有的依赖都是最新的，且符合项目的版本管理策略。
```shell
./gradlew baselineUpdate

```

#### 12. 用于执行单元测试，确保项目的代码质量。执行完该任务后，会生成测试报告，以便开发人员查看测试结果。
```shell
./gradlew baselineJacoco
```

#### 13. 用于与 Maven 相关的任务，尤其是 Maven 发布相关任务。

```shell
./gradlew baselineMaven
```

