import * as React from 'react';

import { StyleSheet, View, Button } from 'react-native';
import { enableKioskMode, disableKioskMode } from 'react-native-kiosk';

export default function App() {
  const [isKioskModeEnabled, setIsKioskModeEnabled] = React.useState<boolean>();

  return (
    <View style={styles.container}>
      <Button
        onPress={async () => {
          if (isKioskModeEnabled) {
            await disableKioskMode();
            setIsKioskModeEnabled(false);
            return;
          }
          await enableKioskMode();
          setIsKioskModeEnabled(true);
        }}
        title={isKioskModeEnabled ? 'Disable Kiosk' : 'Enable Kiosk'}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
