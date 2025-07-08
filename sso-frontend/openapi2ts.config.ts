const config = {
  requestLibPath: "import { request } from '@/utils/requestForManager.ts'",
  schemaPath: "http://manager.sso.auth.com:8082/v2/api-docs?group=manager",
  mock: false,
  projectName: 'api',
  outputDir: 'src/services',
};

export default config;
