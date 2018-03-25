'use-strict';

import HomePage from './HomePage';
import ReceivePage from './ReceivePage';
import SendPage from './SendPage';
import InfoPage from './InfoPage';
import SplashScreen from './SplashScreen';

import React, { Component } from 'react';
import {
  StackNavigator,
} from 'react-navigation';

const App = StackNavigator(
  {
    Home: { screen: HomePage },
    Receive: { screen: ReceivePage },
    Send: { screen: SendPage },
  },
  {
    initialRouteName: 'Home',
    navigationOptions: {
      headerStyle: {
        backgroundColor: '#298e82',
      },
      headerTintColor: '#fff',
    },
  }
);

const RootStack = StackNavigator(
  {
    Splash: { screen: SplashScreen },
    Main: { screen: App },
    Info: { screen: InfoPage },
  },
  {
    mode: 'modal',
    headerMode: 'none',
  }
);

export default RootStack;
