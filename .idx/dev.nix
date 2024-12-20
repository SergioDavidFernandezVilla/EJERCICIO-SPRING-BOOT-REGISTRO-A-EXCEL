{ pkgs, ... }: {
  # Which nixpkgs channel to use.
  channel = "stable-23.11"; # or "unstable"
  # Use https://search.nixos.org/packages to find packages
  packages = [
    pkgs.zulu17
    pkgs.maven
    pkgs.jdk
    pkgs.postgresql
  ];
  # Sets environment variables in the workspace
  env = {
    PGHOST = "localhost";
    PGPORT = "5432";
    PGUSER = "postgres";
    PGPASSWORD = "naruto12";
    PGDATABASE = "nose";
  };
  idx = {
    extensions = [
      "vscjava.vscode-java-pack"
      "rangav.vscode-thunder-client"
    ];
    workspace = {
      onCreate = {
        # Inicializa el directorio de datos si no existe
        init-postgresql = "initdb -D /workspace/pgdata || true";
        install = "mvn clean install";
      };
      onStart = {
        # Inicia PostgreSQL
        start-postgresql = "pg_ctl -D /workspace/pgdata -l /workspace/logfile start || true";
        # Verifica si PostgreSQL está corriendo
        check-postgresql = "pg_isready || echo 'PostgreSQL no está listo'";
        run-server = "PORT=3000 mvn spring-boot:run";
      };
    };
  };
}
