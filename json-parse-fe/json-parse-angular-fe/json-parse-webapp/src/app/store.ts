export interface IAppState {
  loggedIn: boolean;
  email: string;
  jwtToken: string;
}

export function rootReducer(state: any, action: any) {
  return state
}

export const INITIAL_STATE: IAppState = {
  loggedIn: false,
  email: "user",
  jwtToken: "jwt"
}
