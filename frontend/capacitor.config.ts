import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.nuvora.app',
  appName: 'Nuvora',
  webDir: 'dist/frontend',
  server: {
    androidScheme: 'http'
  }
};

export default config;
