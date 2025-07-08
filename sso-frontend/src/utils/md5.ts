import { md5} from 'js-md5';

export function inputPassToFormPass (inputPass: string) {
  const salt = 'qRdPV)@t';
  return md5(salt + inputPass + salt);
}
